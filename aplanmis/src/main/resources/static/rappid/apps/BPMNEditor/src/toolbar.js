/*! Rappid v2.4.0 - HTML5 Diagramming Framework - TRIAL VERSION

Copyright (c) 2015 client IO

 2019-07-09 


This Source Code Form is subject to the terms of the Rappid Trial License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


window.toolbarConfig = {

    tools: [
       /* {
            type: 'button',
            name: 'load',
            attrs: {
                button: {
                    'data-tooltip': 'Load',
                }
            }
        },*/
        {
            type: 'button',
            name: 'save',
            attrs: {
                button: {
                    // 'data-tooltip': 'Save'
                    'data-tooltip': '保存'
                }
            }
        },
       /* {
            type: 'button',
            name: 'print',
            attrs: {
                button: {
                    'data-tooltip': 'Open a Print Dialog'
                }
            }
        },*/
        {
            type: 'button',
            name: 'tojson',
            attrs: {
                button: {
                    // 'data-tooltip': 'Open As JSON'
                    'data-tooltip': 'json串'
                }
            }
        },
     /*   {
            type: 'zoom-in',
            name: '放大',
            attrs: {
                button: {
                    'data-tooltip': '放大'
                }
            }
        },
        {
            type: 'zoom-out',
            name: '缩小',
            attrs: {
                button: {
                    'data-tooltip': '缩小'
                }
            }
        },*/
        {
            type: 'button',
            name: 'clear',
            attrs: {
                button: {
                    // 'data-tooltip': 'Clear Paper'
                    'data-tooltip': '清除'
                }
            }
        },
        /*{
            type: 'undo',
            name: 'undo',
            attrs: {
                button: {
                    'data-tooltip': 'Undo'
                }
            }
        },
        {
            type: 'redo',
            name: 'redo',
            attrs: {
                button: {
                    'data-tooltip': 'Redo'
                }
            }
        },*/
      /*  {
            type: 'redo',
            name: 'retract',
            attrs: {
                button: {
                    'data-tooltip': 'retract'
                }
            }
        }*/
    ]
};