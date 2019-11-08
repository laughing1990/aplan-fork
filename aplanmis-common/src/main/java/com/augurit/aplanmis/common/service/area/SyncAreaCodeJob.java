package com.augurit.aplanmis.common.service.area;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.helper.JedisHelper;
import com.augurit.aplanmis.common.vo.AreaVo;
import com.augurit.aplanmis.common.vo.CityVo;
import com.augurit.aplanmis.common.vo.ProvinceVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tiantian
 * @date 2019/1/22
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class SyncAreaCodeJob {

    private final AtomicBoolean isSyncAreaCode = new AtomicBoolean(false);

    //http://www.mca.gov.cn/article/sj/xzqh/2018/201804-12/20181101021046.html
    @Value("${sync-area-base-url:http://www.mca.gov.cn/article/sj/xzqh/2019/201901-06/201902061009.html}")
    private String syncBaseUrl;

    @Value("${sync-area-town-url:http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/index.html}")
    private String syncTownUrl;

    @Value("${sync-area-town-url:http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/}")
    private String syncTownBaseUrl;

    private final String SYNC_AREA_CODE_DATA_KEY = "syncAreaCodeData";

    private final String SYNC_AREA_CODE_DATA_FILE_NAME = "syncAreaCodeData.txt";


    private final String AREA_CODE_KEY_HEAD = "areaCode_";

    public final String RUNING_DIRECTORY = "aplanmis"+File.separator+"runingDirectory";

    @Autowired
    private JedisHelper jedisUtil;

    /**
     * 没启用redis缓存的话使用此字段的数据
     */
    private List<ProvinceVo> areaCodeList = new ArrayList<>();

    /**
     * 是否启用redis缓存
     */
    private boolean useRedis = true;

    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    public void syncAreaCodejob(){
        String jobName = "同步行政区划代码";
        synchronized (isSyncAreaCode) {
            if(isSyncAreaCode.get()) {
                log.info(jobName + " - 任务进行中");
                return;
            }
            isSyncAreaCode.set(true);
        }
        try{
            long startTime = System.currentTimeMillis();
            log.info(jobName + " － 任务开始");
            syncAreaCode();
            log.info(jobName + " － 任务结束 {}ms", System.currentTimeMillis() - startTime);
        }catch(Exception e){
            log.error(jobName + " - " + e.toString(), e);
        }finally {
            isSyncAreaCode.set(false);
        }
    }

    public List<ProvinceVo> syncAreaCode()throws Exception{
        Document doc = Jsoup.connect(syncBaseUrl).maxBodySize(0).get();
        Elements elements = doc.getElementsByClass("xl7016597");
        List<String> stringList = elements.eachText();
        List<String> stringName = new ArrayList<>();
        List<String> stringCode = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            if (i % 2 == 0) {
                //地区代码
                if(StringUtils.isNotBlank(stringList.get(i))) {
                    stringCode.add(stringList.get(i));
                }
            } else {
                //地区名字
                if(StringUtils.isNotBlank(stringList.get(i))) {
                    stringName.add(stringList.get(i));
                }
            }
        }
        if (stringName.size() != stringCode.size()) {
            throw new RuntimeException("数据错误");
        }
        List<ProvinceVo> provinceList = processData(stringName, stringCode);

        if(provinceList!=null && !provinceList.isEmpty()) {
            String json = JSONObject.toJSONString(provinceList);
            if(useRedis) {
                saveRedisWithoutException(SYNC_AREA_CODE_DATA_KEY, json);
            }else{
                areaCodeList = new ArrayList<>();
                areaCodeList.addAll(provinceList);
            }
            writeToFile(json);
            if(useRedis) {
                saveProvince(provinceList);
            }
        }

        return provinceList;

    }

    private void saveProvince(List<ProvinceVo> provinceList){
        for(ProvinceVo p:provinceList){
            ProvinceVo province = new ProvinceVo();
            province.setName(p.getName());
            province.setCode(p.getCode());
            List<CityVo> cityList = p.getCityList();
            List<CityVo> cList = new ArrayList<>();
            if(cityList!=null){
                for(CityVo city: cityList ){
                    CityVo c = new CityVo();
                    c.setName(city.getName());
                    c.setCode(city.getCode());
                    cList.add(c);
                    saveCity(city);
                }
            }
            province.setCityList(cList);
            jedisUtil.set(AREA_CODE_KEY_HEAD+province.getCode(), JSONObject.toJSONString(province));
        }
    }

    private void saveCity(CityVo city){
        jedisUtil.set(AREA_CODE_KEY_HEAD+city.getCode(), JSONObject.toJSONString(city));
        List<AreaVo> areaList = city.getAreaList();
        if(areaList!=null){
            for(AreaVo area:areaList){
                saveArea(area);
            }
        }
    }

    private void saveArea(AreaVo area){
        jedisUtil.set(AREA_CODE_KEY_HEAD+area.getCode(), JSONObject.toJSONString(area));
    }

    /**
     * 生成省份列表数据
     *
     * 行政区划代码一共六位，前两位代表省，第三、四位代表市，第五六位代表县、区。
     * 如果后四位为0，那么这一行为省。
     * 如果只有后两位为0，那么为地级市
     * 其他的为县
     * 香港，台湾，澳门比较特殊，没有对应的市区
     *
     */
    private List<ProvinceVo> processData(List<String> stringName, List<String> stringCode) throws Exception{
        List<ProvinceVo> provinceList = new ArrayList<>();
        for (int i = 0; i < stringCode.size(); i++) {
            String provinceName = stringName.get(i);
            String provinceCode = stringCode.get(i);
            if (provinceCode.endsWith("0000")) {//省份
                ProvinceVo province = new ProvinceVo();
                provinceList.add(province);
                province.setCode(provinceCode);
                province.setName(provinceName);
                List<CityVo> cities = new ArrayList<>();
                province.setCityList(cities);
                //香港，澳门，台湾，没有市级行政单位划分，城市 地区 和省份保持一致
                if (provinceName.contains("香港") || provinceName.contains("澳门") || provinceName.contains("台湾")) {
                    CityVo city = new CityVo();
                    List<AreaVo> areas = new ArrayList<>();
                    city.setName(provinceName);
                    city.setCode(provinceCode);
                    city.setAreaList(areas);
                    cities.add(city);
                    AreaVo area = new AreaVo();
                    area.setName(provinceName);
                    area.setCode(provinceCode);
                    areas.add(area);
                }
                //直辖市 城市和省份名称一样
                if (provinceName.contains("北京") || provinceName.contains("上海") || provinceName.contains("天津") || provinceName.contains("重庆")) {
                    CityVo city = new CityVo();
                    List<AreaVo> areas = new ArrayList<>();
                    city.setName(provinceName);
                    city.setCode(provinceCode);
                    city.setAreaList(areas);
                    cities.add(city);
                    //县区
                    for (int k = 0; k < stringCode.size(); k++) {
                        String areaName = stringName.get(k);
                        String areaCode = stringCode.get(k);
                        if (!provinceCode.equals(areaCode) && areaCode.startsWith(provinceCode.substring(0, 2))) {
                            AreaVo area = new AreaVo();
                            area.setName(areaName);
                            area.setCode(areaCode);
                            areas.add(area);
                        }
                    }
                    //没有县区的话取乡级代码
                    if(areas.size()==0){
                        getTownData(areas,provinceName,provinceName);
                    }
                }
                for (int j = 0; j < stringCode.size(); j++) {
                    String cityName = stringName.get(j);
                    String cityCode = stringCode.get(j);
                    //遍历获取地级市
                    if (!cityCode.equals(provinceCode) && cityCode.startsWith(provinceCode.substring(0, 2)) && cityCode.endsWith("00")) {
                        CityVo city = new CityVo();
                        List<AreaVo> areas = new ArrayList<>();
                        city.setName(cityName);
                        city.setCode(cityCode);
                        city.setAreaList(areas);
                        cities.add(city);
                        //遍历获取县区
                        for (int k = 0; k < stringCode.size(); k++) {
                            String areaName = stringName.get(k);
                            String areaCode = stringCode.get(k);
                            if (!areaCode.equals(cityCode) && areaCode.startsWith(cityCode.substring(0, 4))) {
                                AreaVo area = new AreaVo();
                                area.setName(areaName);
                                area.setCode(areaCode);
                                areas.add(area);
                            }
                        }
                        //没有县区的话取乡级代码
                        if(areas.size()==0){
                            getTownData(areas,provinceName,cityName);
                        }
                    }
                }
            }
        }
        return provinceList;
    }

    private void getTownData(List<AreaVo> areas, String provinceName, String cityName)throws Exception{
        Document doc = Jsoup.parse(new URL(syncTownUrl).openStream(), "GBK", syncTownUrl);
        Elements elements = doc.getElementsByClass("provincetr");
        for(Element element:elements){
            Elements tds = element.getElementsByTag("td");
            for(Element td:tds) {
                Element a = td.getElementsByTag("a").first();
                if (a.text().contains(provinceName)) {
                    String href = a.attr("href");
                    href = syncTownBaseUrl + href;
                    Document cityDoc = Jsoup.parse(new URL(href).openStream(), "GBK", href);
                    Elements cityTrs = cityDoc.getElementsByClass("citytr");
                    for (Element cityTr : cityTrs) {
                        Elements as = cityTr.getElementsByTag("a");
                        String cName = as.get(1).text();
                        if (cName.contains(cityName)) {
                            href = href.substring(0, href.lastIndexOf("/")+1) + as.get(1).attr("href");
                            Document areaDoc = Jsoup.parse(new URL(href).openStream(), "GBK", href);
                            Elements towntrs = areaDoc.getElementsByClass("towntr");
                            for (Element towntr : towntrs) {
                                Elements towns = towntr.getElementsByTag("a");
                                String townCode = towns.get(0).text();
                                String townName = towns.get(1).text();
                                AreaVo area = new AreaVo();
                                area.setName(townName);
                                area.setCode(townCode);
                                areas.add(area);
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

//    private void writeToFile(String text) throws Exception{
//        FileWriter fileWritter = null;
//        try {
//            String path = SyncAreaCodeJob.class.getClassLoader().getResource("").getPath();
//            File file = new File(path+SYNC_AREA_CODE_DATA_FILE_NAME);
//            if(!file.exists()){
//                file.createNewFile();
//            }
//            fileWritter = new FileWriter(file.getPath(),false);
//            fileWritter.write(text);
//            fileWritter.flush();
//        } catch (Exception e){
//            throw e;
//        } finally {
//            if(fileWritter!=null){
//                fileWritter.close();
//            }
//        }
//    }

    private void writeToFile(String text) throws Exception{

        FileWriter fileWritter = null;
        try {

            File file = new File(RUNING_DIRECTORY);

            if(!file.exists()){
                file.mkdirs();
            }

            file = new File(RUNING_DIRECTORY+File.separator+SYNC_AREA_CODE_DATA_FILE_NAME);

            if(!file.exists()){
                file.createNewFile();
            }

            fileWritter = new FileWriter(file.getPath(),false);
            fileWritter.write(text);
            fileWritter.flush();
        } catch (Exception e){
            throw e;
        } finally {
            if(fileWritter!=null){
                fileWritter.close();
            }
        }

    }


    public List<ProvinceVo> getSyncAreaCodeData() {
        try {

            String data;

            if(useRedis) {

                data = getRedisDataWithoutException(SYNC_AREA_CODE_DATA_KEY);

                if (StringUtils.isNotBlank(data)) {
                    List<ProvinceVo> list = JSONObject.parseArray(data, ProvinceVo.class);
                    if (list != null && !list.isEmpty()) {
                        return list;
                    }
                }
            }else{
                if(!areaCodeList.isEmpty()){
                    return areaCodeList;
                }
            }

            data = getFileData();

            if(StringUtils.isNotBlank(data)){
                try {

                    if(useRedis) {
                        saveRedisWithoutException(SYNC_AREA_CODE_DATA_KEY,data);
                    }

                    List<ProvinceVo> list = JSONObject.parseArray(data,ProvinceVo.class);

                    if(list!=null && !list.isEmpty()){
                        if(useRedis) {
                            saveProvince(list);
                        }else{
                            areaCodeList = new ArrayList<>();
                            areaCodeList.addAll(list);
                        }
                        return list;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }

            return syncAreaCode();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        return null;
    }

    private String getFileData(){
        try {
            String filecontent = getFileDataFromLocalFile();

            if (StringUtils.isBlank(filecontent)) {
                InputStream inputStream = SyncAreaCodeJob.class.getResourceAsStream("/"+SYNC_AREA_CODE_DATA_FILE_NAME);
                if (inputStream != null) {
                    return IOUtils.toString(inputStream, "utf-8");
                }
            }

            return filecontent;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return null;
    }

    private String getFileDataFromLocalFile(){
        try {
            File file = new File(RUNING_DIRECTORY+File.separator+SYNC_AREA_CODE_DATA_FILE_NAME);
            if(!file.exists()){
                return null;
            }

            return FileUtils.readFileToString(file,"utf-8");
        }catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        return null;
    }

    public List<CityVo> getCityData(String provinceCode){
        String json = null;
        if(useRedis) {
            json = jedisUtil.get(AREA_CODE_KEY_HEAD + provinceCode);
        }

        if (StringUtils.isNotBlank(json)) {
            ProvinceVo province = JSONObject.parseObject(json, ProvinceVo.class);
            return province.getCityList();
        } else {
            List<ProvinceVo> provinceList = getSyncAreaCodeData();
            if (provinceList != null) {
                for (ProvinceVo province : provinceList) {
                    if (province.getCode().equals(provinceCode)) {
                        return province.getCityList();
                    }
                }
            }

        }


        return null;
    }

    public List<AreaVo> getAreaData(String cityCode){
        String json = null;
        if(useRedis) {
            json = jedisUtil.get(AREA_CODE_KEY_HEAD + cityCode);
        }

        if(StringUtils.isNotBlank(json)){
            CityVo city = JSONObject.parseObject(json, CityVo.class);
            return city.getAreaList();
        }else{
            List<ProvinceVo> provinceList = getSyncAreaCodeData();
            if(provinceList!=null){
                for(ProvinceVo province :provinceList){
                    if(province.getCityList()!=null){
                        for(CityVo city:province.getCityList()){
                            if(city.getCode().equals(cityCode)){
                                return city.getAreaList();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private void saveRedisWithoutException(String key, String value) {
        try {
            jedisUtil.set(key, value);
        }catch (Exception e){

        }
    }

    private String getRedisDataWithoutException(String key) {
        try {
            return jedisUtil.get(key);
        }catch (Exception e){

        }

        return null;
    }

}
