var vue = new Vue({
    el: '#etl-job-log',
    data: function () {
        return {
            etlJobLogPage: {
                pageSize: 10,
                pageNum: 1,
                total: 0
            },
            etlErrorLogPage: {
                list: [],
                pageSize: 10,
                pageNum: 1,
                total: 0
            },
            currentJobLogId: '',
            etlJob: {},
            timeRange: '',
            pageNum: 1,
            pageSize: 10,
            errorPageNum: 1,
            errorPageSize: 10,
            etlJobLogList: [],
            logLoading: false,
            etlJobDetailLogList: [],
            detailLogLoading: false,
            errorlogLoading:false,
            isFilterEmpty: true,
            isRelation: true,
            pickerOptions: {
                shortcuts: [{
                    text: '当天',
                    onClick(picker) {
                        var now = new Date();
                        var dayStr = formatDate(now, 'yyyy-MM-dd');
                        picker.$emit('pick', [dayStr, dayStr]);
                    }
                }, {
                    text: '本周',
                    onClick(picker) {
                        var now = new Date();
                        var w = now.getDay() - 1;
                        if (w === 0) {
                            w = 7;
                        }
                        var start = new Date();
                        start.setTime(now.getTime() - 3600 * 1000 * 24 * w);
                        var end = new Date();
                        end.setTime(now.getTime() + 3600 * 1000 * 24 * (6 - w));
                        var startStr = formatDate(start, 'yyyy-MM-dd');
                        var endtStr = formatDate(end, 'yyyy-MM-dd');
                        picker.$emit('pick', [startStr, endtStr]);
                    }
                }, {
                    text: '本月',
                    onClick(picker) {
                        var start = new Date();
                        start.setDate(1);
                        // 获取当前月的最后一天
                        var date = new Date();
                        var currentMonth = date.getMonth();
                        var nextMonth = ++currentMonth;
                        var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
                        var oneDay = 1000 * 60 * 60 * 24;
                        var end = new Date(nextMonthFirstDay - oneDay);
                        var startStr = formatDate(start, 'yyyy-MM-dd');
                        var endtStr = formatDate(end, 'yyyy-MM-dd');
                        picker.$emit('pick', [startStr, endtStr]);
                    }
                }]
            }
        }
    },
    mounted: function () {
        var _that = this;
        _that.timeRange = _that.getToday();
        _that.listEtlJobLog();
        var count = 0;
        window.xunhun = null;

        function oneExecute() {
            if (count % 300 == 0) {
                _that.getRunStatus();
            }
            count++;
            window.xunhun = window.requestAnimationFrame(oneExecute);
        }

        window.requestAnimationFrame(oneExecute);
    },
    methods: {
        handTimeRangeChange: function (value) {
            var _that = this;
            _that.timeRange = value;
            _that.listEtlJobLog();
        },
        handPageNumChange: function (value) {
            var _that = this;
            if (typeof(value) != "undefined") {
                _that.pageNum = value;
            }
            _that.listEtlJobLog();
        },
        handleSizeChange: function (value) {
            var _that = this;
            _that.pageSize = value;
            _that.listEtlJobLog();
        },
        handFilterChange: function (value) {
            var _that = this;
            if (typeof(value) != "undefined") {
                _that.pageNum = 1;
                _that.isFilterEmpty = value;
            }
            _that.listEtlJobLog();
        },
        listEtlJobLog: function () {
            var _that = this;
            var params = {
                isFilterEmpty: _that.isFilterEmpty,
                page: _that.pageNum,
                rows: _that.pageSize,
                startTime: _that.timeRange[0],
                endTime: _that.timeRange[1]
            };
            _that.logLoading = true;
            request('', {
                url: ctx + 'etl/job/log/list',
                type: 'get',
                data: params
            }, function (result) {
                _that.etlJobLogPage = result;
                _that.etlJobLogList = result.list;
                _that.logLoading = false;
                if (result.list != null && result.list.length > 0) {
                    _that.selectJobLog(result.list[0]);
                }
            }, function (msg) {
                _that.logLoading = false;
            })
        },
        selectJobLog: function (row) {
            var _that = this;
            _that.currentJobLogId = row.jobLogId;
            _that.listEtlJobDetailLog(row);
            if (_that.isRelation) {
                _that.listErrorJobLog();
            }
        },
        listEtlJobDetailLog: function (jobLog) {
            var _that = this;
            var params = {
                jobLogId: jobLog.jobLogId
            };
            _that.detailLogLoading = true;
            request('', {
                url: ctx + 'etl/job/log/details',
                type: 'get',
                data: params
            }, function (result) {
                _that.etlJobDetailLogList = result.content;
                _that.detailLogLoading = false;
            }, function (msg) {
                _that.detailLogLoading = false;
            })
        },
        getLogTotal: function (param) {
            var data = param.data;
            var columns = param.columns;
            var sums = [];
            for (var i = 0, len = columns.length; i < len; i++) {
                var column = columns[i];
                if (i == 0) {
                    sums[i] = "";
                    continue;
                }
                if (i == 1) {
                    sums[i] = "总和";
                    continue;
                }
                if (i == 8) {
                    sums[i] = "-";
                    continue;
                }
                var subSum = 0;
                if (i == 6 || i == 7) {
                    subSum = '-';
                }
                for (var x = 0, len2 = data.length; x < len2; x++) {
                    var item = data[x];
                    var propertyVaule = item[column.property];
                    if (propertyVaule) {
                        if (i == 6) {
                            var date = new Date(Date.parse(propertyVaule.replace(/-/g, "/")));
                            if (x == 0) {
                                subSum = date;
                            } else if (subSum > date) {
                                subSum = date;
                            }
                        } else if (i == 7) {
                            var date = new Date(Date.parse(propertyVaule.replace(/-/g, "/")));
                            if (x == 0) {
                                subSum = date;
                            } else if (subSum < date) {
                                subSum = date;
                            }
                        } else {
                            var value = Number(propertyVaule);
                            subSum += value;
                        }
                    }
                }
                if (data.length > 0 && (i == 6 || i == 7) && subSum != '-') {
                    subSum = formatDate(subSum, "yyyy-MM-dd hh:mm:ss");
                }
                sums[i] = subSum;
            }
            return sums;
        },
        getToday: function () {
            var _that = this;
            var now = new Date();
            var dayStr = formatDate(now, 'yyyy-MM-dd');
            return [dayStr, dayStr];
        },
        getRunStatus: function () {
            var _that = this;
            request('', {
                url: ctx + 'etl/job/log/status',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.etlJob = result.content;
                }
            }, function (msg) {
                _that.$message.error('加载运行状态失败' + msg);
            })
        },
        deleteConfirm: function () {
            var _that = this;
            _that.$confirm('确定删除选中的日志么, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                var logs = _that.$refs.etlJobLogTable.selection;
                var logIds = logs.map(function (log) {
                    return log.jobLogId;
                });
                logIds = logIds.join(",");
                _that.deleteEltJobLog(logIds);
            }).catch(function () {
            });
        },
        deleteEltJobLog: function (logIds) {
            var _that = this;
            request('', {
                url: ctx + 'etl/job/log/' + logIds,
                type: 'delete',
            }, function (result) {
                if (result.success) {
                    _that.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    _that.listEtlJobLog();
                } else {
                    _that.$message.error('删除失败' + result.message);
                }
            }, function (msg) {
                _that.$message.error('删除失败' + msg);
            })
        },
        handErrorPageNumChange: function (value) {
            var _that = this;
            if (typeof(value) != "undefined") {
                _that.errorPageNum = value;
            }
            _that.listErrorJobLog();
        },
        handleErrorSizeChange: function (value) {
            var _that = this;
            _that.errorPageSize = value;
            _that.listErrorJobLog();
        },
        listErrorJobLog: function () {
            var _that = this;
            var params = {
                page: _that.errorPageNum,
                rows: _that.errorPageSize
            };
            if (_that.isRelation) {
                params['jobLogId'] = _that.currentJobLogId;
            }
            _that.errorlogLoading = true;
            request('', {
                url: ctx + 'etl/error/log/list',
                type: 'get',
                data: params
            }, function (result) {
                _that.etlErrorLogPage = result.content;
                _that.errorlogLoading = false;
            }, function (msg) {
                _that.errorlogLoading = false;
            })
        },
    }
});