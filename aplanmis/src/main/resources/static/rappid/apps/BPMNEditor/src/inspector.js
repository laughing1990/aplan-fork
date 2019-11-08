/*! Rappid v2.4.0 - HTML5 Diagramming Framework - TRIAL VERSION

Copyright (c) 2015 client IO

 2019-07-09 


This Source Code Form is subject to the terms of the Rappid Trial License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


window.inputs = {

    'bpmn.Gateway': {
        icon: {
            type: 'select',
            options: [
                { value: 'none', content: 'default' },
                { value: 'cross', content: 'exclusive' },
                { value: 'circle', content: 'inclusive' },
                { value: 'plus', content: 'parallel' }
            ],
            label: 'Type',
            group: 'general',
            index: 2
        },
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.body/fill': {
                type: 'color',
                label: 'Body Color ',
                group: 'appearance',
                index: 1
            }
        }
    },

    'bpmn.Activity': {
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 1
        },
        icon: {
            type: 'select',
            options: ['none','message','user'],
            label: 'Icon',
            group: 'general',
            index: 2
        },
        activityType: {
            type: 'select',
            options: ['task', 'transaction', 'event-sub-process', 'call-activity'],
            label: 'Type',
            group: 'general',
            index: 3
        },
        subProcess: {
            type: 'toggle',
            label: 'Sub-process',
            group: 'general',
            index: 4
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            'item/isOptionItem': {
                type: 'toggle',
                label: '可选事项',
                group: '高级属性',
                index: 2
            },
            'item/itemVerId': {
                type: 'text',
                label: '绑定事项',
                // item:[{value:'task', content:'11'}, {value:'task1', content:'12'}, {value:'task2', content:'13'}, {value:'task3', content:'14'}],
                group: '高级属性',
                index: 3,
                // multiple:true
            },
        }
    },

    'bpmn.Event': {
        eventType: {
            type: 'select',
            options: ['start','end','intermediate'],
            group: 'general',
            label: 'Type',
            index: 2
        },
        icon: {
            type: 'select',
            options: [
                { value: 'none', content: 'none' },
                { value: 'cross', content: 'cancel' },
                { value: 'message', content: 'message' },
                { value: 'plus', content: 'parallel multiple' }
            ],
            label: 'Subtype',
            group: 'general',
            index: 3
        },
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            }
        }
    },

    'bpmn.Annotation': {
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 1
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.body/fill-opacity': {
                type: 'range',
                min: 0,
                max: 1,
                step: 0.1,
                label: 'Transparency',
                group: 'appearance',
                index: 2
            }

        }
    },

    'bpmn.Pool': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'parallel/parallels': {
                type: 'select',
                label: '并行',
                options: [],
                group: '高级属性',
                index: 5
            },
        }
    }, 'bpmn.SPool': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'parallel/parallels': {
                type: 'select',
                label: '并行',
                options: [],
                group: '高级属性',
                index: 5
            },
        }
    },
    'bpmn.HPool': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'stage/stageCode': {
                type: 'text',
                label: '编码',
                group: '高级属性',
                index: 6
            },
            'stage/dueNum': {
                type: 'text',
                label: '承诺时限',
                group: '高级属性',
                index: 8
            },
            'stage/dueUnit': {
                type: 'select',
                label: '承诺时限单位',
                group: '高级属性',
                index: 7
            },
            'stage/isNode': {
                type: 'toggle',
                label: '是否主线',
                group: '高级属性',
                index: 10
            },
            'stage/useEl': {
                type: 'toggle',
                label: '启用EL',
                group: '高级属性',
                index: 11
            },
            'stage/handWay': {
                type: 'select',
                label: '办理方式',
                options: ['按阶段多级情形组织事项办理', '多事项直接合并办理'],
                group: '高级属性',
                index: 12
            },
            'stage/lcbsxlx': {
                type: 'select',
                label: '里程碑事项类型',
                options: ['所有里程碑事项办结，该审批阶段才算办结', '任一项里程碑事项办结，该审批阶段就算办结'],
                group: '高级属性',
                index: 13
            },
            'stage/dybzspjdxh': {
                type: 'select',
                label: '对应国家标准审批阶段',
                options: ['工程建设许可','施工许可', '竣工验收','并行推进'],
                group: '高级属性',
                index: 14
            },
        }
    },'bpmn.HPool1': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'stage/stageCode': {
                type: 'text',
                label: '编码',
                group: '高级属性',
                index: 6
            },
            'stage/dueNum': {
                type: 'text',
                label: '承诺时限',
                group: '高级属性',
                index: 8
            },
            'stage/dueUnit': {
                type: 'select',
                label: '承诺时限单位',
                group: '高级属性',
                index: 7
            },
            'stage/isNode': {
                type: 'toggle',
                label: '是否主线',
                group: '高级属性',
                index: 10
            },
            'stage/useEl': {
                type: 'toggle',
                label: '启用EL',
                group: '高级属性',
                index: 11
            },
            'stage/handWay': {
                type: 'select',
                label: '办理方式',
                options: ['按阶段多级情形组织事项办理', '多事项直接合并办理'],
                group: '高级属性',
                index: 12
            },
            'stage/lcbsxlx': {
                type: 'select',
                label: '里程碑事项类型',
                options: ['所有里程碑事项办结，该审批阶段才算办结', '任一项里程碑事项办结，该审批阶段就算办结'],
                group: '高级属性',
                index: 13
            },
            'stage/dybzspjdxh': {
                type: 'select',
                label: '对应国家标准审批阶段',
                options: ['工程建设许可','施工许可', '竣工验收','并行推进'],
                group: '高级属性',
                index: 14
            },
        }
    },'bpmn.HPool2': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'stage/stageCode': {
                type: 'text',
                label: '编码',
                group: '高级属性',
                index: 6
            },
            'stage/dueNum': {
                type: 'text',
                label: '承诺时限',
                group: '高级属性',
                index: 8
            },
            'stage/dueUnit': {
                type: 'select',
                label: '承诺时限单位',
                group: '高级属性',
                index: 7
            },
            'stage/isNode': {
                type: 'toggle',
                label: '是否主线',
                group: '高级属性',
                index: 10
            },
            'stage/useEl': {
                type: 'toggle',
                label: '启用EL',
                group: '高级属性',
                index: 11
            },
            'stage/handWay': {
                type: 'select',
                label: '办理方式',
                options: ['按阶段多级情形组织事项办理', '多事项直接合并办理'],
                group: '高级属性',
                index: 12
            },
            'stage/lcbsxlx': {
                type: 'select',
                label: '里程碑事项类型',
                options: ['所有里程碑事项办结，该审批阶段才算办结', '任一项里程碑事项办结，该审批阶段就算办结'],
                group: '高级属性',
                index: 13
            },
            'stage/dybzspjdxh': {
                type: 'select',
                label: '对应国家标准审批阶段',
                options: ['工程建设许可','施工许可', '竣工验收','并行推进'],
                group: '高级属性',
                index: 14
            },
        }
    },'bpmn.HPool3': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'stage/stageCode': {
                type: 'text',
                label: '编码',
                group: '高级属性',
                index: 6
            },
            'stage/dueNum': {
                type: 'text',
                label: '承诺时限',
                group: '高级属性',
                index: 8
            },
            'stage/dueUnit': {
                type: 'select',
                label: '承诺时限单位',
                group: '高级属性',
                index: 7
            },
            'stage/isNode': {
                type: 'toggle',
                label: '是否主线',
                group: '高级属性',
                index: 10
            },
            'stage/useEl': {
                type: 'toggle',
                label: '启用EL',
                group: '高级属性',
                index: 11
            },
            'stage/handWay': {
                type: 'select',
                label: '办理方式',
                options: ['按阶段多级情形组织事项办理', '多事项直接合并办理'],
                group: '高级属性',
                index: 12
            },
            'stage/lcbsxlx': {
                type: 'select',
                label: '里程碑事项类型',
                options: ['所有里程碑事项办结，该审批阶段才算办结', '任一项里程碑事项办结，该审批阶段就算办结'],
                group: '高级属性',
                index: 13
            },
            'stage/dybzspjdxh': {
                type: 'select',
                label: '对应国家标准审批阶段',
                options: ['工程建设许可','施工许可', '竣工验收','并行推进'],
                group: '高级属性',
                index: 14
            },
        }
    },'bpmn.HPool4': {
        lanes: {
            type: 'object',
            group: 'general',
            index: 1,
            attrs: {
                label: {
                    style: 'display:none;'
                }
            },
            properties: {
                label: {
                    type: 'text',
                    label: 'Label'
                },
                label:{
                    type:'text',
                    label:'阶段名称'
                }
            }
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.header/fill': {
                type: 'color',
                label: 'Header Color',
                group: 'appearance',
                index: 2
            },
            '.lane-body/fill': {
                type: 'color',
                label: 'Lane Body Color',
                group: 'appearance',
                index: 3
            },
            '.lane-header/fill': {
                type: 'color',
                label: 'Lane Header Color',
                group: 'appearance',
                index: 4
            },
            'stage/stageCode': {
                type: 'text',
                label: '编码',
                group: '高级属性',
                index: 6
            },
            'stage/dueNum': {
                type: 'text',
                label: '承诺时限',
                group: '高级属性',
                index: 8
            },
            'stage/dueUnit': {
                type: 'select',
                label: '承诺时限单位',
                group: '高级属性',
                index: 7
            },
            'stage/isNode': {
                type: 'toggle',
                label: '是否主线',
                group: '高级属性',
                index: 10
            },
            'stage/useEl': {
                type: 'toggle',
                label: '启用EL',
                group: '高级属性',
                index: 11
            },
            'stage/handWay': {
                type: 'select',
                label: '办理方式',
                options: ['按阶段多级情形组织事项办理', '多事项直接合并办理'],
                group: '高级属性',
                index: 12
            },
            'stage/lcbsxlx': {
                type: 'select',
                label: '里程碑事项类型',
                options: ['所有里程碑事项办结，该审批阶段才算办结', '任一项里程碑事项办结，该审批阶段就算办结'],
                group: '高级属性',
                index: 13
            },
            'stage/dybzspjdxh': {
                type: 'select',
                label: '对应国家标准审批阶段',
                options: ['工程建设许可','施工许可', '竣工验收','并行推进'],
                group: '高级属性',
                index: 14
            },
        }
    },

    'bpmn.Group': {
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.label-rect/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            }
        }
    },

    'bpmn.Conversation': {
        conversationType: {
            type: 'select',
            options: ['conversation', 'call-conversation'],
            label: 'Type',
            group: 'general',
            index: 2
        },
        subProcess: {
            type: 'toggle',
            label: 'Sub-process',
            group: 'general',
            index: 3
        },
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            }

        }
    },

    'bpmn.Choreography': {
        participants: {
            type: 'list',
            label: 'Particpants',
            item: {
                type: 'text'
            },
            group: 'general',
            index: 1
        },
        initiatingParticipant: {
            type: 'select',
            label: 'Initiating Participant',
            options: 'participants',
            group: 'general',
            index: 2
        },
        subProcess: {
            type: 'toggle',
            label: 'Sub-process',
            group: 'general',
            index: 3
        },
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 4
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Primary Color',
                group: 'appearance',
                index: 1
            },
            '.participant-rect/fill': {
                type: 'color',
                label: 'Secondary Color',
                group: 'appearance',
                index: 2
            }
        }
    },

    'bpmn.DataObject': {
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            }
        }
    },

    'bpmn.Message': {
        attrs: {
            '.label/text': {
                type: 'text',
                label: 'Name',
                group: 'general',
                index: 1
            },
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            }
        }
    },
    'bpmn.Text': {
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 1
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.body/fill-opacity': {
                type: 'range',
                min: 0,
                max: 1,
                step: 0.1,
                label: 'Transparency',
                group: 'appearance',
                index: 2
            }

        }
    },'bpmn.TextD': {
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 1
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.body/fill-opacity': {
                type: 'range',
                min: 0,
                max: 1,
                step: 0.1,
                label: 'Transparency',
                group: 'appearance',
                index: 2
            }

        }
    },'bpmn.TextB': {
        content: {
            type: 'textarea',
            label: 'Content',
            group: 'general',
            index: 1
        },
        attrs: {
            '.body/fill': {
                type: 'color',
                label: 'Body Color',
                group: 'appearance',
                index: 1
            },
            '.body/fill-opacity': {
                type: 'range',
                min: 0,
                max: 1,
                step: 0.1,
                label: 'Transparency',
                group: 'appearance',
                index: 2
            }

        }
    },

    'bpmn.Flow': {
        flowType: {
            type: 'select',
            options: ['default', 'conditional','normal','message','association','conversation'],
            label: 'Type',
            group: 'general',
            index: 1
        }
    }
};
