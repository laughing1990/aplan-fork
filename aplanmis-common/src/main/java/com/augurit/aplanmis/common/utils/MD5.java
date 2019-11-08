//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.augurit.aplanmis.common.utils;

public class MD5 {
    private byte[] PADDING = new byte[]{-128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private long[] state = new long[4];
    private long[] count = new long[2];
    private byte[] buffer = new byte[64];
    public String digestHexStr;
    private byte[] digest = new byte[16];
    private final String[] strDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "i", "z"};

    public String getMD5ofStr(String s) {
        this.md5Init();
        this.md5Update(s.getBytes(), s.length());
        this.md5Final();
        this.digestHexStr = "";

        for(int i = 0; i < 16; ++i) {
            this.digestHexStr = this.digestHexStr + this.byteHEX(this.digest[i]);
        }

        return this.digestHexStr;
    }

    public MD5() {
        this.md5Init();
    }

    private void md5Init() {
        this.count[0] = 0L;
        this.count[1] = 0L;
        this.state[0] = 1732584193L;
        this.state[1] = 4023233417L;
        this.state[2] = 2562383102L;
        this.state[3] = 271733878L;
    }

    private long F(long l, long l1, long l2) {
        return l & l1 | ~l & l2;
    }

    private long G(long l, long l1, long l2) {
        return l & l2 | l1 & ~l2;
    }

    private long H(long l, long l1, long l2) {
        return l ^ l1 ^ l2;
    }

    private long I(long l, long l1, long l2) {
        return l1 ^ (l | ~l2);
    }

    private long FF(long l, long l1, long l2, long l3, long l4, long l5, long l6) {
        l += this.F(l1, l2, l3) + l4 + l6;
        l = (long)((int)l << (int)l5 | (int)l >>> (int)(32L - l5));
        l += l1;
        return l;
    }

    private long GG(long l, long l1, long l2, long l3, long l4, long l5, long l6) {
        l += this.G(l1, l2, l3) + l4 + l6;
        l = (long)((int)l << (int)l5 | (int)l >>> (int)(32L - l5));
        l += l1;
        return l;
    }

    private long HH(long l, long l1, long l2, long l3, long l4, long l5, long l6) {
        l += this.H(l1, l2, l3) + l4 + l6;
        l = (long)((int)l << (int)l5 | (int)l >>> (int)(32L - l5));
        l += l1;
        return l;
    }

    private long II(long l, long l1, long l2, long l3, long l4, long l5, long l6) {
        l += this.I(l1, l2, l3) + l4 + l6;
        l = (long)((int)l << (int)l5 | (int)l >>> (int)(32L - l5));
        l += l1;
        return l;
    }

    private void md5Update(byte[] abyte0, int i) {
        byte[] abyte1 = new byte[64];
        int k = (int)(this.count[0] >>> 3) & 63;
        long[] var10000 = this.count;
        if ((var10000[0] += (long)(i << 3)) < (long)(i << 3)) {
            long var10002 = this.count[1]++;
        }

        var10000 = this.count;
        var10000[1] += (long)(i >>> 29);
        int l = 64 - k;
        int j;
        if (i >= l) {
            this.md5Memcpy(this.buffer, abyte0, k, 0, l);
            this.md5Transform(this.buffer);

            for(j = l; j + 63 < i; j += 64) {
                this.md5Memcpy(abyte1, abyte0, 0, j, 64);
                this.md5Transform(abyte1);
            }

            k = 0;
        } else {
            j = 0;
        }

        this.md5Memcpy(this.buffer, abyte0, k, j, i - j);
    }

    private void md5Final() {
        byte[] abyte0 = new byte[8];
        this.Encode(abyte0, this.count, 8);
        int i = (int)(this.count[0] >>> 3) & 63;
        int j = i >= 56 ? 120 - i : 56 - i;
        this.md5Update(this.PADDING, j);
        this.md5Update(abyte0, 8);
        this.Encode(this.digest, this.state, 16);
    }

    private void md5Memcpy(byte[] abyte0, byte[] abyte1, int i, int j, int k) {
        for(int l = 0; l < k; ++l) {
            abyte0[i + l] = abyte1[j + l];
        }

    }

    private void md5Transform(byte[] abyte0) {
        long l = this.state[0];
        long l1 = this.state[1];
        long l2 = this.state[2];
        long l3 = this.state[3];
        long[] al = new long[16];
        this.Decode(al, abyte0, 64);
        l = this.FF(l, l1, l2, l3, al[0], 7L, 3614090360L);
        l3 = this.FF(l3, l, l1, l2, al[1], 12L, 3905402710L);
        l2 = this.FF(l2, l3, l, l1, al[2], 17L, 606105819L);
        l1 = this.FF(l1, l2, l3, l, al[3], 22L, 3250441966L);
        l = this.FF(l, l1, l2, l3, al[4], 7L, 4118548399L);
        l3 = this.FF(l3, l, l1, l2, al[5], 12L, 1200080426L);
        l2 = this.FF(l2, l3, l, l1, al[6], 17L, 2821735955L);
        l1 = this.FF(l1, l2, l3, l, al[7], 22L, 4249261313L);
        l = this.FF(l, l1, l2, l3, al[8], 7L, 1770035416L);
        l3 = this.FF(l3, l, l1, l2, al[9], 12L, 2336552879L);
        l2 = this.FF(l2, l3, l, l1, al[10], 17L, 4294925233L);
        l1 = this.FF(l1, l2, l3, l, al[11], 22L, 2304563134L);
        l = this.FF(l, l1, l2, l3, al[12], 7L, 1804603682L);
        l3 = this.FF(l3, l, l1, l2, al[13], 12L, 4254626195L);
        l2 = this.FF(l2, l3, l, l1, al[14], 17L, 2792965006L);
        l1 = this.FF(l1, l2, l3, l, al[15], 22L, 1236535329L);
        l = this.GG(l, l1, l2, l3, al[1], 5L, 4129170786L);
        l3 = this.GG(l3, l, l1, l2, al[6], 9L, 3225465664L);
        l2 = this.GG(l2, l3, l, l1, al[11], 14L, 643717713L);
        l1 = this.GG(l1, l2, l3, l, al[0], 20L, 3921069994L);
        l = this.GG(l, l1, l2, l3, al[5], 5L, 3593408605L);
        l3 = this.GG(l3, l, l1, l2, al[10], 9L, 38016083L);
        l2 = this.GG(l2, l3, l, l1, al[15], 14L, 3634488961L);
        l1 = this.GG(l1, l2, l3, l, al[4], 20L, 3889429448L);
        l = this.GG(l, l1, l2, l3, al[9], 5L, 568446438L);
        l3 = this.GG(l3, l, l1, l2, al[14], 9L, 3275163606L);
        l2 = this.GG(l2, l3, l, l1, al[3], 14L, 4107603335L);
        l1 = this.GG(l1, l2, l3, l, al[8], 20L, 1163531501L);
        l = this.GG(l, l1, l2, l3, al[13], 5L, 2850285829L);
        l3 = this.GG(l3, l, l1, l2, al[2], 9L, 4243563512L);
        l2 = this.GG(l2, l3, l, l1, al[7], 14L, 1735328473L);
        l1 = this.GG(l1, l2, l3, l, al[12], 20L, 2368359562L);
        l = this.HH(l, l1, l2, l3, al[5], 4L, 4294588738L);
        l3 = this.HH(l3, l, l1, l2, al[8], 11L, 2272392833L);
        l2 = this.HH(l2, l3, l, l1, al[11], 16L, 1839030562L);
        l1 = this.HH(l1, l2, l3, l, al[14], 23L, 4259657740L);
        l = this.HH(l, l1, l2, l3, al[1], 4L, 2763975236L);
        l3 = this.HH(l3, l, l1, l2, al[4], 11L, 1272893353L);
        l2 = this.HH(l2, l3, l, l1, al[7], 16L, 4139469664L);
        l1 = this.HH(l1, l2, l3, l, al[10], 23L, 3200236656L);
        l = this.HH(l, l1, l2, l3, al[13], 4L, 681279174L);
        l3 = this.HH(l3, l, l1, l2, al[0], 11L, 3936430074L);
        l2 = this.HH(l2, l3, l, l1, al[3], 16L, 3572445317L);
        l1 = this.HH(l1, l2, l3, l, al[6], 23L, 76029189L);
        l = this.HH(l, l1, l2, l3, al[9], 4L, 3654602809L);
        l3 = this.HH(l3, l, l1, l2, al[12], 11L, 3873151461L);
        l2 = this.HH(l2, l3, l, l1, al[15], 16L, 530742520L);
        l1 = this.HH(l1, l2, l3, l, al[2], 23L, 3299628645L);
        l = this.II(l, l1, l2, l3, al[0], 6L, 4096336452L);
        l3 = this.II(l3, l, l1, l2, al[7], 10L, 1126891415L);
        l2 = this.II(l2, l3, l, l1, al[14], 15L, 2878612391L);
        l1 = this.II(l1, l2, l3, l, al[5], 21L, 4237533241L);
        l = this.II(l, l1, l2, l3, al[12], 6L, 1700485571L);
        l3 = this.II(l3, l, l1, l2, al[3], 10L, 2399980690L);
        l2 = this.II(l2, l3, l, l1, al[10], 15L, 4293915773L);
        l1 = this.II(l1, l2, l3, l, al[1], 21L, 2240044497L);
        l = this.II(l, l1, l2, l3, al[8], 6L, 1873313359L);
        l3 = this.II(l3, l, l1, l2, al[15], 10L, 4264355552L);
        l2 = this.II(l2, l3, l, l1, al[6], 15L, 2734768916L);
        l1 = this.II(l1, l2, l3, l, al[13], 21L, 1309151649L);
        l = this.II(l, l1, l2, l3, al[4], 6L, 4149444226L);
        l3 = this.II(l3, l, l1, l2, al[11], 10L, 3174756917L);
        l2 = this.II(l2, l3, l, l1, al[2], 15L, 718787259L);
        l1 = this.II(l1, l2, l3, l, al[9], 21L, 3951481745L);
        long[] var10000 = this.state;
        var10000[0] += l;
        var10000 = this.state;
        var10000[1] += l1;
        var10000 = this.state;
        var10000[2] += l2;
        var10000 = this.state;
        var10000[3] += l3;
    }

    private void Encode(byte[] abyte0, long[] al, int i) {
        int j = 0;

        for(int k = 0; k < i; k += 4) {
            abyte0[k] = (byte)((int)(al[j] & 255L));
            abyte0[k + 1] = (byte)((int)(al[j] >>> 8 & 255L));
            abyte0[k + 2] = (byte)((int)(al[j] >>> 16 & 255L));
            abyte0[k + 3] = (byte)((int)(al[j] >>> 24 & 255L));
            ++j;
        }

    }

    private void Decode(long[] al, byte[] abyte0, int i) {
        int j = 0;

        for(int k = 0; k < i; k += 4) {
            al[j] = this.b2iu(abyte0[k]) | this.b2iu(abyte0[k + 1]) << 8 | this.b2iu(abyte0[k + 2]) << 16 | this.b2iu(abyte0[k + 3]) << 24;
            ++j;
        }

    }

    public long b2iu(byte byte0) {
        return (long)(byte0 >= 0 ? byte0 : byte0 & 255);
    }

    public String byteHEX(byte byte0) {
        char[] ac = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ac1 = new char[]{ac[byte0 >>> 4 & 15], ac[byte0 & 15]};
        String s = new String(ac1);
        return s;
    }

    private String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (bByte < 0) {
            iRet = bByte + 256;
        }

        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return this.strDigits[iD1] + this.strDigits[iD2];
    }

    private String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();

        for(int i = 0; i < bByte.length; ++i) {
            sBuffer.append(this.byteToArrayString(bByte[i]));
        }

        return sBuffer.toString();
    }

    public static String GetMD5Code(byte[] bytObj) {
        MD5 md5 = new MD5();
        String resultString = md5.byteToString(bytObj);
        return md5.getMD5ofStr(resultString).toLowerCase();
    }

    public static String GetMD5Code(String strObj) {
        MD5 md5 = new MD5();
        return md5.getMD5ofStr(strObj).toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(GetMD5Code("123456"));
        System.out.println(GetMD5Code("123456".getBytes()));
    }
}
