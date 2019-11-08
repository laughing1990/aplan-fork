(function(n, t) {
    typeof exports == "object" && typeof module == "object" ? module.exports = t() : typeof define == "function" && define.amd ? define("pdfjs-dist/build/pdf", [], t) : typeof exports == "object" ? exports["pdfjs-dist/build/pdf"] = t() : n["pdfjs-dist/build/pdf"] = n.pdfjsDistBuildPdf = t()
})(this,
function() {
    return function(n) {
        function t(r) {
            if (i[r]) return i[r].exports;
            var u = i[r] = {
                i: r,
                l: !1,
                exports: {}
            };
            return n[r].call(u.exports, u, u.exports, t),
            u.l = !0,
            u.exports
        }
        var i = {};
        return t.m = n,
        t.c = i,
        t.d = function(n, i, r) {
            t.o(n, i) || Object.defineProperty(n, i, {
                configurable: !1,
                enumerable: !0,
                get: r
            })
        },
        t.n = function(n) {
            var i = n && n.__esModule ?
            function() {
                return n["default"]
            }: function() {
                return n
            };
            return t.d(i, "a", i),
            i
        },
        t.o = function(n, t) {
            return Object.prototype.hasOwnProperty.call(n, t)
        },
        t.p = "",
        t(t.s = 63)
    } ([function(n, t, i) {
        "use strict";
        function rt(n) {
            c = n
        }
        function ut() {
            return c
        }
        function ft(n) {
            c >= h.infos && console.log("Info: " + n)
        }
        function o(n) {
            c >= h.warnings && console.log("Warning: " + n)
        }
        function et(n) {
            console.log("Deprecated API usage: " + n)
        }
        function k(n) {
            throw new Error(n);
        }
        function r(n, t) {
            n || k(t)
        }
        function ot(n, t) {
            var i, r;
            try {
                if (i = new URL(n), !i.origin || i.origin === "null") return ! 1
            } catch(u) {
                return ! 1
            }
            return r = new URL(t, i),
            i.origin === r.origin
        }
        function st(n) {
            if (!n) return ! 1;
            switch (n.protocol) {
            case "http:":
            case "https:":
            case "ftp:":
            case "mailto:":
            case "tel:":
                return ! 0;
            default:
                return ! 1
            }
        }
        function ht(n, t) {
            if (!n) return null;
            try {
                var i = t ? new URL(n, t) : new URL(n);
                if (st(i)) return i
            } catch(r) {}
            return null
        }
        function ct(n, t, i) {
            return Object.defineProperty(n, t, {
                value: i,
                enumerable: !0,
                configurable: !0,
                writable: !1
            }),
            i
        }
        function lt(n) {
            var t;
            return function() {
                return n && (t = Object.create(null), n(t), n = null),
                t
            }
        }
        function dt(n) {
            return typeof n != "string" ? (o("The argument for removeNullCharacters must be a string."), n) : n.replace(kt, "")
        }
        function gt(n) {
            var i, u, f, t, o, s;
            if (r(n !== null && (typeof n == "undefined" ? "undefined": e(n)) === "object" && n.length !== undefined, "Invalid argument for bytesToString"), i = n.length, u = 8192, i < u) return String.fromCharCode.apply(null, n);
            for (f = [], t = 0; t < i; t += u) o = Math.min(t + u, i),
            s = n.subarray(t, o),
            f.push(String.fromCharCode.apply(null, s));
            return f.join("")
        }
        function g(n) {
            var i, u, t;
            for (r(typeof n == "string", "Invalid argument for stringToBytes"), i = n.length, u = new Uint8Array(i), t = 0; t < i; ++t) u[t] = n.charCodeAt(t) & 255;
            return u
        }
        function nt(n) {
            return n.length !== undefined ? n.length: (r(n.byteLength !== undefined), n.byteLength)
        }
        function ni(n) {
            var u, i, f, t, r, e, o;
            if (n.length === 1 && n[0] instanceof Uint8Array) return n[0];
            for (u = 0, f = n.length, i = 0; i < f; i++) t = n[i],
            r = nt(t),
            u += r;
            for (e = 0, o = new Uint8Array(u), i = 0; i < f; i++) t = n[i],
            t instanceof Uint8Array || (t = typeof t == "string" ? g(t) : new Uint8Array(t)),
            r = t.byteLength,
            o.set(t, e),
            e += r;
            return o
        }
        function ti(n) {
            return String.fromCharCode(n >> 24 & 255, n >> 16 & 255, n >> 8 & 255, n & 255)
        }
        function ii(n) {
            for (var t = 1,
            i = 0; n > t;) t <<= 1,
            i++;
            return i
        }
        function ri(n, t) {
            return n[t] << 24 >> 24
        }
        function ui(n, t) {
            return n[t] << 8 | n[t + 1]
        }
        function fi(n, t) {
            return (n[t] << 24 | n[t + 1] << 16 | n[t + 2] << 8 | n[t + 3]) >>> 0
        }
        function ei() {
            var n = new Uint8Array(4),
            t;
            return n[0] = 1,
            t = new Uint32Array(n.buffer, 0, 1),
            t[0] === 1
        }
        function oi() {
            try {
                return new Function(""),
                !0
            } catch(n) {
                return ! 1
            }
        }
        function ci(n) {
            var t, u = n.length,
            i = [],
            r;
            if (n[0] === "t" && n[1] === "?") for (t = 2; t < u; t += 2) i.push(String.fromCharCode(n.charCodeAt(t) << 8 | n.charCodeAt(t + 1)));
            else for (t = 0; t < u; ++t) r = hi[n.charCodeAt(t)],
            i.push(r ? String.fromCharCode(r) : n.charAt(t));
            return i.join("")
        }
        function li(n) {
            return decodeURIComponent(escape(n))
        }
        function ai(n) {
            return unescape(encodeURIComponent(n))
        }
        function vi(n) {
            for (var t in n) return ! 1;
            return ! 0
        }
        function yi(n) {
            return typeof n == "boolean"
        }
        function pi(n) {
            return typeof n == "number"
        }
        function wi(n) {
            return typeof n == "string"
        }
        function bi(n) {
            return (typeof n == "undefined" ? "undefined": e(n)) === "object" && n !== null && n.byteLength !== undefined
        }
        function ki(n) {
            return n === 32 || n === 9 || n === 13 || n === 10
        }
        function di() {
            return (typeof process == "undefined" ? "undefined": e(process)) === "object" && process + "" == "[object process]"
        }
        function u() {
            var n = {};
            return n.promise = new Promise(function(t, i) {
                n.resolve = t;
                n.reject = i
            }),
            n
        }
        function p(n, t) {
            var i = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : null;
            return n ? new Promise(function(r) {
                r(n.apply(i, t))
            }) : Promise.resolve(undefined)
        }
        function f(n) {
            if ((typeof n == "undefined" ? "undefined": e(n)) !== "object") return n;
            switch (n.name) {
            case "AbortException":
                return new y(n.message);
            case "MissingPDFException":
                return new a(n.message);
            case "UnexpectedResponseException":
                return new v(n.message, n.status);
            default:
                return new l(n.message, n.details)
            }
        }
        function tr(n) {
            return ! (n instanceof Error) || n instanceof y || n instanceof a || n instanceof v || n instanceof l ? n: new l(n.message, n.toString())
        }
        function w(n, t, i) {
            t ? n.resolve() : n.reject(i)
        }
        function ir(n) {
            return Promise.resolve(n).
            catch(function() {})
        }
        function it(n, t, i) {
            var r = this,
            u, e;
            this.sourceName = n;
            this.targetName = t;
            this.comObj = i;
            this.callbackId = 1;
            this.streamId = 1;
            this.postMessageTransfers = !0;
            this.streamSinks = Object.create(null);
            this.streamControllers = Object.create(null);
            u = this.callbacksCapabilities = Object.create(null);
            e = this.actionHandler = Object.create(null);
            this._onComObjOnMessage = function(n) {
                var t = n.data,
                s, h, o, c, l;
                if (t.targetName === r.sourceName) if (t.stream) r._processStreamMessage(t);
                else if (t.isReply) if (s = t.callbackId, t.callbackId in u) h = u[s],
                delete u[s],
                "error" in t ? h.reject(f(t.error)) : h.resolve(t.data);
                else throw new Error("Cannot resolve callback " + s);
                else if (t.action in e) o = e[t.action],
                t.callbackId ? (c = r.sourceName, l = t.sourceName, Promise.resolve().then(function() {
                    return o[0].call(o[1], t.data)
                }).then(function(n) {
                    i.postMessage({
                        sourceName: c,
                        targetName: l,
                        isReply: !0,
                        callbackId: t.callbackId,
                        data: n
                    })
                },
                function(n) {
                    i.postMessage({
                        sourceName: c,
                        targetName: l,
                        isReply: !0,
                        callbackId: t.callbackId,
                        error: tr(n)
                    })
                })) : t.streamId ? r._createStreamSink(t) : o[0].call(o[1], t.data);
                else throw new Error("Unknown action from worker: " + t.action);
            };
            i.addEventListener("message", this._onComObjOnMessage)
        }
        function rr(n, t, i) {
            var r = new Image;
            r.onload = function() {
                i.resolve(n, r)
            };
            r.onerror = function() {
                i.resolve(n, null);
                o("Error during JPEG image loading")
            };
            r.src = t
        }
        var e, d;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.unreachable = t.warn = t.utf8StringToString = t.stringToUTF8String = t.stringToPDFString = t.stringToBytes = t.string32 = t.shadow = t.setVerbosityLevel = t.ReadableStream = t.removeNullCharacters = t.readUint32 = t.readUint16 = t.readInt8 = t.log2 = t.loadJpegStream = t.isEvalSupported = t.isLittleEndian = t.createValidAbsoluteUrl = t.isSameOrigin = t.isNodeJS = t.isSpace = t.isString = t.isNum = t.isEmptyObj = t.isBool = t.isArrayBuffer = t.info = t.getVerbosityLevel = t.getLookupTableFactory = t.deprecated = t.createObjectURL = t.createPromiseCapability = t.createBlob = t.bytesToString = t.assert = t.arraysToBytes = t.arrayByteLength = t.FormatError = t.XRefParseException = t.Util = t.UnknownErrorException = t.UnexpectedResponseException = t.TextRenderingMode = t.StreamType = t.StatTimer = t.PasswordResponses = t.PasswordException = t.PageViewport = t.NotImplementedException = t.NativeImageDecoding = t.MissingPDFException = t.MissingDataException = t.MessageHandler = t.InvalidPDFException = t.AbortException = t.CMapCompressionType = t.ImageKind = t.FontType = t.AnnotationType = t.AnnotationFlag = t.AnnotationFieldFlag = t.AnnotationBorderStyleType = t.UNSUPPORTED_FEATURES = t.VERBOSITY_LEVELS = t.OPS = t.IDENTITY_MATRIX = t.FONT_IDENTITY_MATRIX = undefined;
        e = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        };
        i(64);
        var b = i(111),
        h = {
            errors: 0,
            warnings: 1,
            infos: 5
        },
        c = h.warnings;
        d = {
            unknown: "unknown",
            forms: "forms",
            javaScript: "javaScript",
            smask: "smask",
            shadingPattern: "shadingPattern",
            font: "font"
        };
        var at = function() {
            function n(n, t) {
                this.name = "PasswordException";
                this.message = n;
                this.code = t
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        l = function() {
            function n(n, t) {
                this.name = "UnknownErrorException";
                this.message = n;
                this.details = t
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        vt = function() {
            function n(n) {
                this.name = "InvalidPDFException";
                this.message = n
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        a = function() {
            function n(n) {
                this.name = "MissingPDFException";
                this.message = n
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        v = function() {
            function n(n, t) {
                this.name = "UnexpectedResponseException";
                this.message = n;
                this.status = t
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        yt = function() {
            function n(n) {
                this.message = n
            }
            return n.prototype = new Error,
            n.prototype.name = "NotImplementedException",
            n.constructor = n,
            n
        } (),
        pt = function() {
            function n(n, t) {
                this.begin = n;
                this.end = t;
                this.message = "Missing data [" + n + ", " + t + ")"
            }
            return n.prototype = new Error,
            n.prototype.name = "MissingDataException",
            n.constructor = n,
            n
        } (),
        wt = function() {
            function n(n) {
                this.message = n
            }
            return n.prototype = new Error,
            n.prototype.name = "XRefParseException",
            n.constructor = n,
            n
        } (),
        bt = function() {
            function n(n) {
                this.message = n
            }
            return n.prototype = new Error,
            n.prototype.name = "FormatError",
            n.constructor = n,
            n
        } (),
        y = function() {
            function n(n) {
                this.name = "AbortException";
                this.message = n
            }
            return n.prototype = new Error,
            n.constructor = n,
            n
        } (),
        kt = /\x00/g;
        var s = function() {
            function n() {}
            var t = ["rgb(", 0, ",", 0, ",", 0, ")"],
            i;
            return n.makeCssRgb = function(n, i, r) {
                return t[1] = n,
                t[3] = i,
                t[5] = r,
                t.join("")
            },
            n.transform = function(n, t) {
                return [n[0] * t[0] + n[2] * t[1], n[1] * t[0] + n[3] * t[1], n[0] * t[2] + n[2] * t[3], n[1] * t[2] + n[3] * t[3], n[0] * t[4] + n[2] * t[5] + n[4], n[1] * t[4] + n[3] * t[5] + n[5]]
            },
            n.applyTransform = function(n, t) {
                var i = n[0] * t[0] + n[1] * t[2] + t[4],
                r = n[0] * t[1] + n[1] * t[3] + t[5];
                return [i, r]
            },
            n.applyInverseTransform = function(n, t) {
                var i = t[0] * t[3] - t[1] * t[2],
                r = (n[0] * t[3] - n[1] * t[2] + t[2] * t[5] - t[4] * t[3]) / i,
                u = ( - n[0] * t[1] + n[1] * t[0] + t[4] * t[1] - t[5] * t[0]) / i;
                return [r, u]
            },
            n.getAxialAlignedBoundingBox = function(t, i) {
                var r = n.applyTransform(t, i),
                u = n.applyTransform(t.slice(2, 4), i),
                f = n.applyTransform([t[0], t[3]], i),
                e = n.applyTransform([t[2], t[1]], i);
                return [Math.min(r[0], u[0], f[0], e[0]), Math.min(r[1], u[1], f[1], e[1]), Math.max(r[0], u[0], f[0], e[0]), Math.max(r[1], u[1], f[1], e[1])]
            },
            n.inverseTransform = function(n) {
                var t = n[0] * n[3] - n[1] * n[2];
                return [n[3] / t, -n[1] / t, -n[2] / t, n[0] / t, (n[2] * n[5] - n[4] * n[3]) / t, (n[4] * n[1] - n[5] * n[0]) / t]
            },
            n.apply3dTransform = function(n, t) {
                return [n[0] * t[0] + n[1] * t[1] + n[2] * t[2], n[3] * t[0] + n[4] * t[1] + n[5] * t[2], n[6] * t[0] + n[7] * t[1] + n[8] * t[2]]
            },
            n.singularValueDecompose2dScale = function(n) {
                var t = [n[0], n[2], n[1], n[3]],
                i = n[0] * t[0] + n[1] * t[2],
                e = n[0] * t[1] + n[1] * t[3],
                o = n[2] * t[0] + n[3] * t[2],
                r = n[2] * t[1] + n[3] * t[3],
                u = (i + r) / 2,
                f = Math.sqrt((i + r) * (i + r) - 4 * (i * r - o * e)) / 2,
                s = u + f || 1,
                h = u - f || 1;
                return [Math.sqrt(s), Math.sqrt(h)]
            },
            n.normalizeRect = function(n) {
                var t = n.slice(0);
                return n[0] > n[2] && (t[0] = n[2], t[2] = n[0]),
                n[1] > n[3] && (t[1] = n[3], t[3] = n[1]),
                t
            },
            n.intersect = function(t, i) {
                function e(n, t) {
                    return n - t
                }
                var r = [t[0], t[2], i[0], i[2]].sort(e),
                u = [t[1], t[3], i[1], i[3]].sort(e),
                f = [];
                if (t = n.normalizeRect(t), i = n.normalizeRect(i), r[0] === t[0] && r[1] === i[0] || r[0] === i[0] && r[1] === t[0]) f[0] = r[1],
                f[2] = r[2];
                else return ! 1;
                if (u[0] === t[1] && u[1] === i[1] || u[0] === i[1] && u[1] === t[1]) f[1] = u[1],
                f[3] = u[2];
                else return ! 1;
                return f
            },
            i = ["", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM", "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"],
            n.toRoman = function(n, t) {
                var f, u, e;
                for (r(Number.isInteger(n) && n > 0, "The number should be a positive integer."), u = []; n >= 1e3;) n -= 1e3,
                u.push("M");
                return f = n / 100 | 0,
                n %= 100,
                u.push(i[f]),
                f = n / 10 | 0,
                n %= 10,
                u.push(i[10 + f]),
                u.push(i[20 + n]),
                e = u.join(""),
                t ? e.toLowerCase() : e
            },
            n.appendToArray = function(n, t) {
                Array.prototype.push.apply(n, t)
            },
            n.prependToArray = function(n, t) {
                Array.prototype.unshift.apply(n, t)
            },
            n.extendObj = function(n, t) {
                for (var i in t) n[i] = t[i]
            },
            n.getInheritableProperty = function(n, t, i) {
                while (n && !n.has(t)) n = n.get("Parent");
                return n ? i ? n.getArray(t) : n.get(t) : null
            },
            n.inherit = function(n, t, i) {
                n.prototype = Object.create(t.prototype);
                n.prototype.constructor = n;
                for (var r in i) n.prototype[r] = i[r]
            },
            n.loadScript = function(n, t) {
                var i = document.createElement("script"),
                r = !1;
                i.setAttribute("src", n);
                t && (i.onload = function() {
                    r || t();
                    r = !0
                });
                document.getElementsByTagName("head")[0].appendChild(i)
            },
            n
        } (),
        si = function() {
            function n(n, t, i, r, u, f) {
                var c, l, s, h, e, o, a, v, y, p;
                this.viewBox = n;
                this.scale = t;
                this.rotation = i;
                this.offsetX = r;
                this.offsetY = u;
                c = (n[2] + n[0]) / 2;
                l = (n[3] + n[1]) / 2;
                i = i % 360;
                i = i < 0 ? i + 360 : i;
                switch (i) {
                case 180:
                    s = -1;
                    h = 0;
                    e = 0;
                    o = 1;
                    break;
                case 90:
                    s = 0;
                    h = 1;
                    e = 1;
                    o = 0;
                    break;
                case 270:
                    s = 0;
                    h = -1;
                    e = -1;
                    o = 0;
                    break;
                default:
                    s = 1;
                    h = 0;
                    e = 0;
                    o = -1
                }
                f && (e = -e, o = -o);
                s === 0 ? (a = Math.abs(l - n[1]) * t + r, v = Math.abs(c - n[0]) * t + u, y = Math.abs(n[3] - n[1]) * t, p = Math.abs(n[2] - n[0]) * t) : (a = Math.abs(c - n[0]) * t + r, v = Math.abs(l - n[1]) * t + u, y = Math.abs(n[2] - n[0]) * t, p = Math.abs(n[3] - n[1]) * t);
                this.transform = [s * t, h * t, e * t, o * t, a - s * t * c - e * t * l, v - h * t * c - o * t * l];
                this.width = y;
                this.height = p;
                this.fontScale = t
            }
            return n.prototype = {
                clone: function(t) {
                    t = t || {};
                    var i = "scale" in t ? t.scale: this.scale,
                    r = "rotation" in t ? t.rotation: this.rotation;
                    return new n(this.viewBox.slice(), i, r, this.offsetX, this.offsetY, t.dontFlip)
                },
                convertToViewportPoint: function(n, t) {
                    return s.applyTransform([n, t], this.transform)
                },
                convertToViewportRectangle: function(n) {
                    var t = s.applyTransform([n[0], n[1]], this.transform),
                    i = s.applyTransform([n[2], n[3]], this.transform);
                    return [t[0], t[1], i[0], i[1]]
                },
                convertToPdfPoint: function(n, t) {
                    return s.applyInverseTransform([n, t], this.transform)
                }
            },
            n
        } (),
        hi = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 728, 711, 710, 729, 733, 731, 730, 732, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8226, 8224, 8225, 8230, 8212, 8211, 402, 8260, 8249, 8250, 8722, 8240, 8222, 8220, 8221, 8216, 8217, 8218, 8482, 64257, 64258, 321, 338, 352, 376, 381, 305, 322, 339, 353, 382, 0, 8364];
        var gi = function() {
            function t(n, t, i) {
                while (n.length < i) n += t;
                return n
            }
            function n() {
                this.started = Object.create(null);
                this.times = [];
                this.enabled = !0
            }
            return n.prototype = {
                time: function(n) {
                    this.enabled && (n in this.started && o("Timer is already running for " + n), this.started[n] = Date.now())
                },
                timeEnd: function(n) {
                    this.enabled && (n in this.started || o("Timer has not been started for " + n), this.times.push({
                        name: n,
                        start: this.started[n],
                        end: Date.now()
                    }), delete this.started[n])
                },
                toString: function() {
                    for (var r = this.times,
                    o = "",
                    f = 0,
                    e, u, s, n = 0,
                    i = r.length; n < i; ++n) e = r[n].name,
                    e.length > f && (f = e.length);
                    for (n = 0, i = r.length; n < i; ++n) u = r[n],
                    s = u.end - u.start,
                    o += t(u.name, " ", f) + " " + s + "ms\n";
                    return o
                }
            },
            n
        } (),
        tt = function(n, t) {
            if (typeof Blob != "undefined") return new Blob([n], {
                type: t
            });
            throw new Error('The "Blob" constructor is not supported.');
        },
        nr = function() {
            var n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
            return function(t, i) {
                var c = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : !1,
                e,
                f,
                r,
                u;
                if (!c && URL.createObjectURL) return e = tt(t, i),
                URL.createObjectURL(e);
                for (f = "data:" + i + ";base64,", r = 0, u = t.length; r < u; r += 3) {
                    var o = t[r] & 255,
                    s = t[r + 1] & 255,
                    h = t[r + 2] & 255,
                    l = o >> 2,
                    a = (o & 3) << 4 | s >> 4,
                    v = r + 1 < u ? (s & 15) << 2 | h >> 6 : 64,
                    y = r + 2 < u ? h & 63 : 64;
                    f += n[l] + n[a] + n[v] + n[y]
                }
                return f
            }
        } ();
        it.prototype = {
            on: function(n, t, i) {
                var r = this.actionHandler;
                if (r[n]) throw new Error('There is already an actionName called "' + n + '"');
                r[n] = [t, i]
            },
            send: function(n, t, i) {
                var r = {
                    sourceName: this.sourceName,
                    targetName: this.targetName,
                    action: n,
                    data: t
                };
                this.postMessage(r, i)
            },
            sendWithPromise: function(n, t, i) {
                var f = this.callbackId++,
                e = {
                    sourceName: this.sourceName,
                    targetName: this.targetName,
                    action: n,
                    data: t,
                    callbackId: f
                },
                r = u();
                this.callbacksCapabilities[f] = r;
                try {
                    this.postMessage(e, i)
                } catch(o) {
                    r.reject(o)
                }
                return r.promise
            },
            sendWithStream: function(n, t, i) {
                var r = this,
                f = this.streamId++,
                e = this.sourceName,
                o = this.targetName;
                return new b.ReadableStream({
                    start: function(i) {
                        var s = u();
                        return r.streamControllers[f] = {
                            controller: i,
                            startCall: s,
                            isClosed: !1
                        },
                        r.postMessage({
                            sourceName: e,
                            targetName: o,
                            action: n,
                            streamId: f,
                            data: t,
                            desiredSize: i.desiredSize
                        }),
                        s.promise
                    },
                    pull: function(n) {
                        var t = u();
                        return r.streamControllers[f].pullCall = t,
                        r.postMessage({
                            sourceName: e,
                            targetName: o,
                            stream: "pull",
                            streamId: f,
                            desiredSize: n.desiredSize
                        }),
                        t.promise
                    },
                    cancel: function(n) {
                        var t = u();
                        return r.streamControllers[f].cancelCall = t,
                        r.streamControllers[f].isClosed = !0,
                        r.postMessage({
                            sourceName: e,
                            targetName: o,
                            stream: "cancel",
                            reason: n,
                            streamId: f
                        }),
                        t.promise
                    }
                },
                i)
            },
            _createStreamSink: function(n) {
                var e = this,
                o = this,
                f = this.actionHandler[n.action],
                r = n.streamId,
                s = n.desiredSize,
                h = this.sourceName,
                c = n.sourceName,
                l = u(),
                t = function(n) {
                    var t = n.stream,
                    i = n.chunk,
                    u = n.transfers,
                    f = n.success,
                    o = n.reason;
                    e.postMessage({
                        sourceName: h,
                        targetName: c,
                        stream: t,
                        streamId: r,
                        chunk: i,
                        success: f,
                        reason: o
                    },
                    u)
                },
                i = {
                    enqueue: function(n) {
                        var r = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 1,
                        f = arguments[2],
                        i;
                        this.isCancelled || (i = this.desiredSize, this.desiredSize -= r, i > 0 && this.desiredSize <= 0 && (this.sinkCapability = u(), this.ready = this.sinkCapability.promise), t({
                            stream: "enqueue",
                            chunk: n,
                            transfers: f
                        }))
                    },
                    close: function() {
                        this.isCancelled || (this.isCancelled = !0, t({
                            stream: "close"
                        }), delete o.streamSinks[r])
                    },
                    error: function(n) {
                        this.isCancelled || (this.isCancelled = !0, t({
                            stream: "error",
                            reason: n
                        }))
                    },
                    sinkCapability: l,
                    onPull: null,
                    onCancel: null,
                    isCancelled: !1,
                    desiredSize: s,
                    ready: null
                };
                i.sinkCapability.resolve();
                i.ready = i.sinkCapability.promise;
                this.streamSinks[r] = i;
                p(f[0], [n.data, i], f[1]).then(function() {
                    t({
                        stream: "start_complete",
                        success: !0
                    })
                },
                function(n) {
                    t({
                        stream: "start_complete",
                        success: !1,
                        reason: n
                    })
                })
            },
            _processStreamMessage: function(n) {
                var t = this,
                e = this.sourceName,
                o = n.sourceName,
                s = n.streamId,
                i = function(n) {
                    var i = n.stream,
                    r = n.success,
                    u = n.reason;
                    t.comObj.postMessage({
                        sourceName: e,
                        targetName: o,
                        stream: i,
                        success: r,
                        streamId: s,
                        reason: u
                    })
                },
                u = function() {
                    Promise.all([t.streamControllers[n.streamId].startCall, t.streamControllers[n.streamId].pullCall, t.streamControllers[n.streamId].cancelCall].map(function(n) {
                        return n && ir(n.promise)
                    })).then(function() {
                        delete t.streamControllers[n.streamId]
                    })
                };
                switch (n.stream) {
                case "start_complete":
                    w(this.streamControllers[n.streamId].startCall, n.success, f(n.reason));
                    break;
                case "pull_complete":
                    w(this.streamControllers[n.streamId].pullCall, n.success, f(n.reason));
                    break;
                case "pull":
                    if (!this.streamSinks[n.streamId]) {
                        i({
                            stream: "pull_complete",
                            success: !0
                        });
                        break
                    }
                    this.streamSinks[n.streamId].desiredSize <= 0 && n.desiredSize > 0 && this.streamSinks[n.streamId].sinkCapability.resolve();
                    this.streamSinks[n.streamId].desiredSize = n.desiredSize;
                    p(this.streamSinks[n.streamId].onPull).then(function() {
                        i({
                            stream: "pull_complete",
                            success: !0
                        })
                    },
                    function(n) {
                        i({
                            stream: "pull_complete",
                            success: !1,
                            reason: n
                        })
                    });
                    break;
                case "enqueue":
                    r(this.streamControllers[n.streamId], "enqueue should have stream controller");
                    this.streamControllers[n.streamId].isClosed || this.streamControllers[n.streamId].controller.enqueue(n.chunk);
                    break;
                case "close":
                    if (r(this.streamControllers[n.streamId], "close should have stream controller"), this.streamControllers[n.streamId].isClosed) break;
                    this.streamControllers[n.streamId].isClosed = !0;
                    this.streamControllers[n.streamId].controller.close();
                    u();
                    break;
                case "error":
                    r(this.streamControllers[n.streamId], "error should have stream controller");
                    this.streamControllers[n.streamId].controller.error(f(n.reason));
                    u();
                    break;
                case "cancel_complete":
                    w(this.streamControllers[n.streamId].cancelCall, n.success, f(n.reason));
                    u();
                    break;
                case "cancel":
                    if (!this.streamSinks[n.streamId]) break;
                    p(this.streamSinks[n.streamId].onCancel, [f(n.reason)]).then(function() {
                        i({
                            stream: "cancel_complete",
                            success: !0
                        })
                    },
                    function(n) {
                        i({
                            stream: "cancel_complete",
                            success: !1,
                            reason: n
                        })
                    });
                    this.streamSinks[n.streamId].sinkCapability.reject(f(n.reason));
                    this.streamSinks[n.streamId].isCancelled = !0;
                    delete this.streamSinks[n.streamId];
                    break;
                default:
                    throw new Error("Unexpected stream case");
                }
            },
            postMessage: function(n, t) {
                t && this.postMessageTransfers ? this.comObj.postMessage(n, t) : this.comObj.postMessage(n)
            },
            destroy: function() {
                this.comObj.removeEventListener("message", this._onComObjOnMessage)
            }
        };
        t.FONT_IDENTITY_MATRIX = [.001, 0, 0, .001, 0, 0];
        t.IDENTITY_MATRIX = [1, 0, 0, 1, 0, 0];
        t.OPS = {
            dependency: 1,
            setLineWidth: 2,
            setLineCap: 3,
            setLineJoin: 4,
            setMiterLimit: 5,
            setDash: 6,
            setRenderingIntent: 7,
            setFlatness: 8,
            setGState: 9,
            save: 10,
            restore: 11,
            transform: 12,
            moveTo: 13,
            lineTo: 14,
            curveTo: 15,
            curveTo2: 16,
            curveTo3: 17,
            closePath: 18,
            rectangle: 19,
            stroke: 20,
            closeStroke: 21,
            fill: 22,
            eoFill: 23,
            fillStroke: 24,
            eoFillStroke: 25,
            closeFillStroke: 26,
            closeEOFillStroke: 27,
            endPath: 28,
            clip: 29,
            eoClip: 30,
            beginText: 31,
            endText: 32,
            setCharSpacing: 33,
            setWordSpacing: 34,
            setHScale: 35,
            setLeading: 36,
            setFont: 37,
            setTextRenderingMode: 38,
            setTextRise: 39,
            moveText: 40,
            setLeadingMoveText: 41,
            setTextMatrix: 42,
            nextLine: 43,
            showText: 44,
            showSpacedText: 45,
            nextLineShowText: 46,
            nextLineSetSpacingShowText: 47,
            setCharWidth: 48,
            setCharWidthAndBounds: 49,
            setStrokeColorSpace: 50,
            setFillColorSpace: 51,
            setStrokeColor: 52,
            setStrokeColorN: 53,
            setFillColor: 54,
            setFillColorN: 55,
            setStrokeGray: 56,
            setFillGray: 57,
            setStrokeRGBColor: 58,
            setFillRGBColor: 59,
            setStrokeCMYKColor: 60,
            setFillCMYKColor: 61,
            shadingFill: 62,
            beginInlineImage: 63,
            beginImageData: 64,
            endInlineImage: 65,
            paintXObject: 66,
            markPoint: 67,
            markPointProps: 68,
            beginMarkedContent: 69,
            beginMarkedContentProps: 70,
            endMarkedContent: 71,
            beginCompat: 72,
            endCompat: 73,
            paintFormXObjectBegin: 74,
            paintFormXObjectEnd: 75,
            beginGroup: 76,
            endGroup: 77,
            beginAnnotations: 78,
            endAnnotations: 79,
            beginAnnotation: 80,
            endAnnotation: 81,
            paintJpegXObject: 82,
            paintImageMaskXObject: 83,
            paintImageMaskXObjectGroup: 84,
            paintImageXObject: 85,
            paintInlineImageXObject: 86,
            paintInlineImageXObjectGroup: 87,
            paintImageXObjectRepeat: 88,
            paintImageMaskXObjectRepeat: 89,
            paintSolidColorImageMask: 90,
            constructPath: 91
        };
        t.VERBOSITY_LEVELS = h;
        t.UNSUPPORTED_FEATURES = d;
        t.AnnotationBorderStyleType = {
            SOLID: 1,
            DASHED: 2,
            BEVELED: 3,
            INSET: 4,
            UNDERLINE: 5
        };
        t.AnnotationFieldFlag = {
            READONLY: 1,
            REQUIRED: 2,
            NOEXPORT: 4,
            MULTILINE: 4096,
            PASSWORD: 8192,
            NOTOGGLETOOFF: 16384,
            RADIO: 32768,
            PUSHBUTTON: 65536,
            COMBO: 131072,
            EDIT: 262144,
            SORT: 524288,
            FILESELECT: 1048576,
            MULTISELECT: 2097152,
            DONOTSPELLCHECK: 4194304,
            DONOTSCROLL: 8388608,
            COMB: 16777216,
            RICHTEXT: 33554432,
            RADIOSINUNISON: 33554432,
            COMMITONSELCHANGE: 67108864
        };
        t.AnnotationFlag = {
            INVISIBLE: 1,
            HIDDEN: 2,
            PRINT: 4,
            NOZOOM: 8,
            NOROTATE: 16,
            NOVIEW: 32,
            READONLY: 64,
            LOCKED: 128,
            TOGGLENOVIEW: 256,
            LOCKEDCONTENTS: 512
        };
        t.AnnotationType = {
            TEXT: 1,
            LINK: 2,
            FREETEXT: 3,
            LINE: 4,
            SQUARE: 5,
            CIRCLE: 6,
            POLYGON: 7,
            POLYLINE: 8,
            HIGHLIGHT: 9,
            UNDERLINE: 10,
            SQUIGGLY: 11,
            STRIKEOUT: 12,
            STAMP: 13,
            CARET: 14,
            INK: 15,
            POPUP: 16,
            FILEATTACHMENT: 17,
            SOUND: 18,
            MOVIE: 19,
            WIDGET: 20,
            SCREEN: 21,
            PRINTERMARK: 22,
            TRAPNET: 23,
            WATERMARK: 24,
            THREED: 25,
            REDACT: 26
        };
        t.FontType = {
            UNKNOWN: 0,
            TYPE1: 1,
            TYPE1C: 2,
            CIDFONTTYPE0: 3,
            CIDFONTTYPE0C: 4,
            TRUETYPE: 5,
            CIDFONTTYPE2: 6,
            TYPE3: 7,
            OPENTYPE: 8,
            TYPE0: 9,
            MMTYPE1: 10
        };
        t.ImageKind = {
            GRAYSCALE_1BPP: 1,
            RGB_24BPP: 2,
            RGBA_32BPP: 3
        };
        t.CMapCompressionType = {
            NONE: 0,
            BINARY: 1,
            STREAM: 2
        };
        t.AbortException = y;
        t.InvalidPDFException = vt;
        t.MessageHandler = it;
        t.MissingDataException = pt;
        t.MissingPDFException = a;
        t.NativeImageDecoding = {
            NONE: "none",
            DECODE: "decode",
            DISPLAY: "display"
        };
        t.NotImplementedException = yt;
        t.PageViewport = si;
        t.PasswordException = at;
        t.PasswordResponses = {
            NEED_PASSWORD: 1,
            INCORRECT_PASSWORD: 2
        };
        t.StatTimer = gi;
        t.StreamType = {
            UNKNOWN: 0,
            FLATE: 1,
            LZW: 2,
            DCT: 3,
            JPX: 4,
            JBIG: 5,
            A85: 6,
            AHX: 7,
            CCF: 8,
            RL: 9
        };
        t.TextRenderingMode = {
            FILL: 0,
            STROKE: 1,
            FILL_STROKE: 2,
            INVISIBLE: 3,
            FILL_ADD_TO_PATH: 4,
            STROKE_ADD_TO_PATH: 5,
            FILL_STROKE_ADD_TO_PATH: 6,
            ADD_TO_PATH: 7,
            FILL_STROKE_MASK: 3,
            ADD_TO_PATH_FLAG: 4
        };
        t.UnexpectedResponseException = v;
        t.UnknownErrorException = l;
        t.Util = s;
        t.XRefParseException = wt;
        t.FormatError = bt;
        t.arrayByteLength = nt;
        t.arraysToBytes = ni;
        t.assert = r;
        t.bytesToString = gt;
        t.createBlob = tt;
        t.createPromiseCapability = u;
        t.createObjectURL = nr;
        t.deprecated = et;
        t.getLookupTableFactory = lt;
        t.getVerbosityLevel = ut;
        t.info = ft;
        t.isArrayBuffer = bi;
        t.isBool = yi;
        t.isEmptyObj = vi;
        t.isNum = pi;
        t.isString = wi;
        t.isSpace = ki;
        t.isNodeJS = di;
        t.isSameOrigin = ot;
        t.createValidAbsoluteUrl = ht;
        t.isLittleEndian = ei;
        t.isEvalSupported = oi;
        t.loadJpegStream = rr;
        t.log2 = ii;
        t.readInt8 = ri;
        t.readUint16 = ui;
        t.readUint32 = fi;
        t.removeNullCharacters = dt;
        t.ReadableStream = b.ReadableStream;
        t.setVerbosityLevel = rt;
        t.shadow = ct;
        t.string32 = ti;
        t.stringToBytes = g;
        t.stringToPDFString = ci;
        t.stringToUTF8String = li;
        t.utf8StringToString = ai;
        t.warn = o;
        t.unreachable = k
    },
    function(n) {
        "use strict";
        var t = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        };
        n.exports = function(n) {
            return (typeof n == "undefined" ? "undefined": t(n)) === "object" ? n !== null: typeof n == "function"
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(42)("wks"),
        e = i(20),
        u = i(3).Symbol,
        f = typeof u == "function",
        o = n.exports = function(n) {
            return r[n] || (r[n] = f && u[n] || (f ? u: e)("Symbol." + n))
        };
        o.store = r
    },
    function(n) {
        "use strict";
        var t = n.exports = typeof window != "undefined" && window.Math == Math ? window: typeof self != "undefined" && self.Math == Math ? self: Function("return this")();
        typeof __g == "number" && (__g = t)
    },
    function(n, t, i) {
        "use strict";
        var u = i(3),
        f = i(5),
        s = i(11),
        h = i(9),
        o = i(10),
        e = "prototype",
        r = function r(n, t, i) {
            var d = n & r.F,
            y = n & r.G,
            g = n & r.S,
            w = n & r.P,
            nt = n & r.B,
            a = y ? u: g ? u[t] || (u[t] = {}) : (u[t] || {})[e],
            v = y ? f: f[t] || (f[t] = {}),
            b = v[e] || (v[e] = {}),
            l,
            p,
            c,
            k;
            y && (i = t);
            for (l in i) p = !d && a && a[l] !== undefined,
            c = (p ? a: i)[l],
            k = nt && p ? o(c, u) : w && typeof c == "function" ? o(Function.call, c) : c,
            a && h(a, l, c, n & r.U),
            v[l] != c && s(v, l, k),
            w && b[l] != c && (b[l] = c)
        };
        u.core = f;
        r.F = 1;
        r.G = 2;
        r.S = 4;
        r.P = 8;
        r.B = 16;
        r.W = 32;
        r.U = 64;
        r.R = 128;
        n.exports = r
    },
    function(n) {
        "use strict";
        var t = n.exports = {
            version: "2.5.1"
        };
        typeof __e == "number" && (__e = t)
    },
    function(n, t, i) {
        "use strict";
        var r = i(1);
        n.exports = function(n) {
            if (!r(n)) throw TypeError(n + " is not an object!");
            return n
        }
    },
    function(n) {
        "use strict";
        var t = {}.hasOwnProperty;
        n.exports = function(n, i) {
            return t.call(n, i)
        }
    },
    function(n, t, i) {
        "use strict";
        function v(n) {
            return n && n.__esModule ? n: {
                "default": n
            }
        }
        function e(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        function nt(n, t) {
            var f = t && t.url,
            i, r;
            n.href = n.title = f ? u.removeNullCharacters(f) : "";
            f && (i = t.target, typeof i == "undefined" && (i = o("externalLinkTarget")), n.target = g[i], r = t.rel, typeof r == "undefined" && (r = o("externalLinkRel")), n.rel = r)
        }
        function tt(n) {
            var t = n.indexOf("#"),
            i = n.indexOf("?"),
            r = Math.min(t > 0 ? t: n.length, i > 0 ? i: n.length);
            return n.substring(n.lastIndexOf("/", r) + 1, r)
        }
        function o(n) {
            var t = a.
        default.PDFJS;
            switch (n) {
            case "pdfBug":
                return t ? t.pdfBug: !1;
            case "disableAutoFetch":
                return t ? t.disableAutoFetch: !1;
            case "disableStream":
                return t ? t.disableStream: !1;
            case "disableRange":
                return t ? t.disableRange: !1;
            case "disableFontFace":
                return t ? t.disableFontFace: !1;
            case "disableCreateObjectURL":
                return t ? t.disableCreateObjectURL: !1;
            case "disableWebGL":
                return t ? t.disableWebGL: !0;
            case "cMapUrl":
                return t ? t.cMapUrl: null;
            case "cMapPacked":
                return t ? t.cMapPacked: !1;
            case "postMessageTransfers":
                return t ? t.postMessageTransfers: !0;
            case "workerPort":
                return t ? t.workerPort: null;
            case "workerSrc":
                return t ? t.workerSrc: null;
            case "disableWorker":
                return t ? t.disableWorker: !1;
            case "maxImageSize":
                return t ? t.maxImageSize: -1;
            case "imageResourcesPath":
                return t ? t.imageResourcesPath: "";
            case "isEvalSupported":
                return t ? t.isEvalSupported: !0;
            case "externalLinkTarget":
                if (!t) return r.NONE;
                switch (t.externalLinkTarget) {
                case r.NONE:
                case r.SELF:
                case r.BLANK:
                case r.PARENT:
                case r.TOP:
                    return t.externalLinkTarget
                }
                return u.warn("PDFJS.externalLinkTarget is invalid: " + t.externalLinkTarget),
                t.externalLinkTarget = r.NONE,
                r.NONE;
            case "externalLinkRel":
                return t ? t.externalLinkRel: h;
            case "enableStats":
                return !! (t && t.enableStats);
            default:
                throw new Error("Unknown default setting: " + n);
            }
        }
        function it() {
            var n = o("externalLinkTarget");
            switch (n) {
            case r.NONE:
                return ! 1;
            case r.SELF:
            case r.BLANK:
            case r.PARENT:
            case r.TOP:
                return ! 0
            }
        }
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.SimpleXMLParser = t.DOMSVGFactory = t.DOMCMapReaderFactory = t.DOMCanvasFactory = t.DEFAULT_LINK_REL = t.getDefaultSetting = t.LinkTarget = t.getFilenameFromUrl = t.isExternalLinkTargetSet = t.addLinkAttributes = t.RenderingCancelledException = t.CustomStyle = undefined;
        var f = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        u = i(0),
        l = i(14),
        a = v(l);
        var h = "noopener noreferrer nofollow",
        c = "http://www.w3.org/2000/svg",
        y = function() {
            function n() {
                e(this, n)
            }
            return f(n, [{
                key: "create",
                value: function(n, t) {
                    if (n <= 0 || t <= 0) throw new Error("invalid canvas size");
                    var i = document.createElement("canvas"),
                    r = i.getContext("2d");
                    return i.width = n,
                    i.height = t,
                    {
                        canvas: i,
                        context: r
                    }
                }
            },
            {
                key: "reset",
                value: function(n, t, i) {
                    if (!n.canvas) throw new Error("canvas is not specified");
                    if (t <= 0 || i <= 0) throw new Error("invalid canvas size");
                    n.canvas.width = t;
                    n.canvas.height = i
                }
            },
            {
                key: "destroy",
                value: function(n) {
                    if (!n.canvas) throw new Error("canvas is not specified");
                    n.canvas.width = 0;
                    n.canvas.height = 0;
                    n.canvas = null;
                    n.context = null
                }
            }]),
            n
        } (),
        p = function() {
            function n(t) {
                var i = t.baseUrl,
                u = i === undefined ? null: i,
                r = t.isCompressed,
                f = r === undefined ? !1 : r;
                e(this, n);
                this.baseUrl = u;
                this.isCompressed = f
            }
            return f(n, [{
                key: "fetch",
                value: function(n) {
                    var t = this,
                    i = n.name;
                    return this.baseUrl ? i ? new Promise(function(n, r) {
                        var e = t.baseUrl + i + (t.isCompressed ? ".bcmap": ""),
                        f = new XMLHttpRequest;
                        f.open("GET", e, !0);
                        t.isCompressed && (f.responseType = "arraybuffer");
                        f.onreadystatechange = function() {
                            if (f.readyState === XMLHttpRequest.DONE) {
                                if (f.status === 200 || f.status === 0) {
                                    var i = void 0;
                                    if (t.isCompressed && f.response ? i = new Uint8Array(f.response) : !t.isCompressed && f.responseText && (i = u.stringToBytes(f.responseText)), i) {
                                        n({
                                            cMapData: i,
                                            compressionType: t.isCompressed ? u.CMapCompressionType.BINARY: u.CMapCompressionType.NONE
                                        });
                                        return
                                    }
                                }
                                r(new Error("Unable to load " + (t.isCompressed ? "binary ": "") + "CMap at: " + e))
                            }
                        };
                        f.send(null)
                    }) : Promise.reject(new Error("CMap name must be specified.")) : Promise.reject(new Error('CMap baseUrl must be specified, see "PDFJS.cMapUrl" (and also "PDFJS.cMapPacked").'))
                }
            }]),
            n
        } (),
        w = function() {
            function n() {
                e(this, n)
            }
            return f(n, [{
                key: "create",
                value: function(n, t) {
                    u.assert(n > 0 && t > 0, "Invalid SVG dimensions");
                    var i = document.createElementNS(c, "svg:svg");
                    return i.setAttribute("version", "1.1"),
                    i.setAttribute("width", n + "px"),
                    i.setAttribute("height", t + "px"),
                    i.setAttribute("preserveAspectRatio", "none"),
                    i.setAttribute("viewBox", "0 0 " + n + " " + t),
                    i
                }
            },
            {
                key: "createElement",
                value: function(n) {
                    return u.assert(typeof n == "string", "Invalid SVG element type"),
                    document.createElementNS(c, n)
                }
            }]),
            n
        } (),
        s = function() {
            function n(t, i) {
                e(this, n);
                this.nodeName = t;
                this.nodeValue = i;
                Object.defineProperty(this, "parentNode", {
                    value: null,
                    writable: !0
                })
            }
            return f(n, [{
                key: "hasChildNodes",
                value: function() {
                    return this.childNodes && this.childNodes.length > 0
                }
            },
            {
                key: "firstChild",
                get: function() {
                    return this.childNodes[0]
                }
            },
            {
                key: "nextSibling",
                get: function() {
                    var n = this.parentNode.childNodes.indexOf(this);
                    return this.parentNode.childNodes[n + 1]
                }
            },
            {
                key: "textContent",
                get: function() {
                    return this.childNodes ? this.childNodes.map(function(n) {
                        return n.textContent
                    }).join("") : this.nodeValue || ""
                }
            }]),
            n
        } (),
        b = function() {
            function n() {
                e(this, n)
            }
            return f(n, [{
                key: "parseFromString",
                value: function(n) {
                    var u = this,
                    t = [],
                    r,
                    i;
                    n = n.replace(/<\?[\s\S]*?\?>|<!--[\s\S]*?-->/g, "").trim();
                    n = n.replace(/<!DOCTYPE[^>\[]+(\[[^\]]+)?[^>]+>/g, "").trim();
                    n = n.replace(/>([^<][\s\S]*?)</g,
                    function(n, i) {
                        var f = t.length,
                        r = new s("#text", u._decodeXML(i));
                        return (t.push(r), r.textContent.trim().length === 0) ? "><": ">" + f + ",<"
                    });
                    n = n.replace(/<!\[CDATA\[([\s\S]*?)\]\]>/g,
                    function(n, i) {
                        var r = t.length,
                        u = new s("#text", i);
                        return t.push(u),
                        r + ","
                    });
                    r = /<([\w\:]+)((?:[\s\w:=]|'[^']*'|"[^"]*")*)(?:\/>|>([\d,]*)<\/[^>]+>)/g;
                    i = void 0;
                    do i = t.length,
                    n = n.replace(r,
                    function(n, i, r, u) {
                        var o = t.length,
                        f = new s(i),
                        e = [];
                        return u && (u = u.split(","), u.pop(), u.forEach(function(n) {
                            var i = t[ + n];
                            i.parentNode = f;
                            e.push(i)
                        })),
                        f.childNodes = e,
                        t.push(f),
                        o + ","
                    });
                    while (i < t.length);
                    return {
                        documentElement: t.pop()
                    }
                }
            },
            {
                key: "_decodeXML",
                value: function(n) {
                    return n.indexOf("&") < 0 ? n: n.replace(/&(#(x[0-9a-f]+|\d+)|\w+);/gi,
                    function(n, t, i) {
                        if (i) return i = i[0] === "x" ? parseInt(i.substring(1), 16) : +i,
                        String.fromCharCode(i);
                        switch (t) {
                        case "amp":
                            return "&";
                        case "lt":
                            return "<";
                        case "gt":
                            return ">";
                        case "quot":
                            return '"';
                        case "apos":
                            return "'"
                        }
                        return "&" + t + ";"
                    })
                }
            }]),
            n
        } (),
        k = function() {
            function t() {}
            var i = ["ms", "Moz", "Webkit", "O"],
            n = Object.create(null);
            return t.getProp = function(t, r) {
                var f, e, o, u, s;
                if (arguments.length === 1 && typeof n[t] == "string") return n[t];
                if (r = r || document.documentElement, f = r.style, typeof f[t] == "string") return n[t] = t;
                for (o = t.charAt(0).toUpperCase() + t.slice(1), u = 0, s = i.length; u < s; u++) if (e = i[u] + o, typeof f[e] == "string") return n[t] = e;
                return n[t] = "undefined"
            },
            t.setProp = function(n, t, i) {
                var r = this.getProp(n);
                r !== "undefined" && (t.style[r] = i)
            },
            t
        } (),
        d = function() {
            function n(n, t) {
                this.message = n;
                this.type = t
            }
            return n.prototype = new Error,
            n.prototype.name = "RenderingCancelledException",
            n.constructor = n,
            n
        } (),
        r = {
            NONE: 0,
            SELF: 1,
            BLANK: 2,
            PARENT: 3,
            TOP: 4
        },
        g = ["", "_self", "_blank", "_parent", "_top"];
        t.CustomStyle = k;
        t.RenderingCancelledException = d;
        t.addLinkAttributes = nt;
        t.isExternalLinkTargetSet = it;
        t.getFilenameFromUrl = tt;
        t.LinkTarget = r;
        t.getDefaultSetting = o;
        t.DEFAULT_LINK_REL = h;
        t.DOMCanvasFactory = y;
        t.DOMCMapReaderFactory = p;
        t.DOMSVGFactory = w;
        t.SimpleXMLParser = b
    },
    function(n, t, i) {
        "use strict";
        var s = i(3),
        r = i(11),
        o = i(7),
        u = i(20)("src"),
        f = "toString",
        e = Function[f],
        h = ("" + e).split(f);
        i(5).inspectSource = function(n) {
            return e.call(n)
        }; (n.exports = function(n, t, i, f) {
            var e = typeof i == "function"; (e && (o(i, "name") || r(i, "name", t)), n[t] !== i) && (e && (o(i, u) || r(i, u, n[t] ? "" + n[t] : h.join(String(t)))), n === s ? n[t] = i: f ? n[t] ? n[t] = i: r(n, t, i) : (delete n[t], r(n, t, i)))
        })(Function.prototype, f,
        function() {
            return typeof this == "function" && this[u] || e.call(this)
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(16);
        n.exports = function(n, t, i) {
            if (r(n), t === undefined) return n;
            switch (i) {
            case 1:
                return function(i) {
                    return n.call(t, i)
                };
            case 2:
                return function(i, r) {
                    return n.call(t, i, r)
                };
            case 3:
                return function(i, r, u) {
                    return n.call(t, i, r, u)
                }
            }
            return function() {
                return n.apply(t, arguments)
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(15),
        u = i(25);
        n.exports = i(12) ?
        function(n, t, i) {
            return r.f(n, t, u(1, i))
        }: function(n, t, i) {
            return n[t] = i,
            n
        }
    },
    function(n, t, i) {
        "use strict";
        n.exports = !i(13)(function() {
            return Object.defineProperty({},
            "a", {
                get: function() {
                    return 7
                }
            }).a != 7
        })
    },
    function(n) {
        "use strict";
        n.exports = function(n) {
            try {
                return !! n()
            } catch(t) {
                return ! 0
            }
        }
    },
    function(n) {
        "use strict";
        n.exports = typeof window != "undefined" && window.Math === Math ? window: typeof global != "undefined" && global.Math === Math ? global: typeof self != "undefined" && self.Math === Math ? self: {}
    },
    function(n, t, i) {
        "use strict";
        var r = i(6),
        u = i(39),
        f = i(40),
        e = Object.defineProperty;
        t.f = i(12) ? Object.defineProperty: function(n, t, i) {
            if (r(n), t = f(t, !0), r(i), u) try {
                return e(n, t, i)
            } catch(o) {}
            if ("get" in i || "set" in i) throw TypeError("Accessors not supported!");
            return "value" in i && (n[t] = i.value),
            n
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n) {
            if (typeof n != "function") throw TypeError(n + " is not a function!");
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(26),
        u = i(27);
        n.exports = function(n) {
            return r(u(n))
        }
    },
    function(n) {
        "use strict";
        var t = {}.toString;
        n.exports = function(n) {
            return t.call(n).slice(8, -1)
        }
    },
    function(n) {
        "use strict";
        n.exports = {}
    },
    function(n) {
        "use strict";
        var t = 0,
        i = Math.random();
        n.exports = function(n) {
            return "Symbol(".concat(n === undefined ? "": n, ")_", (++t + i).toString(36))
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(68),
        u = i(43);
        n.exports = Object.keys ||
        function(n) {
            return r(n, u)
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(15).f,
        f = i(7),
        r = i(2)("toStringTag");
        n.exports = function(n, t, i) {
            n && !f(n = i ? n: n.prototype, r) && u(n, r, {
                configurable: !0,
                value: t
            })
        }
    },
    function(n, t, i) {
        "use strict";
        var e = i(10),
        o = i(87),
        s = i(88),
        h = i(6),
        c = i(28),
        l = i(89),
        r = {},
        u = {},
        f = n.exports = function(n, t, i, f, a) {
            var w = a ?
            function() {
                return n
            }: l(n),
            b = e(i, f, t ? 2 : 1),
            y = 0,
            d,
            p,
            k,
            v;
            if (typeof w != "function") throw TypeError(n + " is not iterable!");
            if (s(w)) {
                for (d = c(n.length); d > y; y++) if (v = t ? b(h(p = n[y])[0], p[1]) : b(n[y]), v === r || v === u) return v
            } else for (k = w.call(n); ! (p = k.next()).done;) if (v = o(k, b, p.value, t), v === r || v === u) return v
        };
        f.BREAK = r;
        f.RETURN = u
    },
    function(n, t, i) {
        "use strict";
        var u = i(1),
        r = i(3).document,
        f = u(r) && u(r.createElement);
        n.exports = function(n) {
            return f ? r.createElement(n) : {}
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n, t) {
            return {
                enumerable: !(n & 1),
                configurable: !(n & 2),
                writable: !(n & 4),
                value: t
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(18);
        n.exports = Object("z").propertyIsEnumerable(0) ? Object: function(n) {
            return r(n) == "String" ? n.split("") : Object(n)
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n) {
            if (n == undefined) throw TypeError("Can't call method on  " + n);
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(29),
        u = Math.min;
        n.exports = function(n) {
            return n > 0 ? u(r(n), 9007199254740991) : 0
        }
    },
    function(n) {
        "use strict";
        var t = Math.ceil,
        i = Math.floor;
        n.exports = function(n) {
            return isNaN(n = +n) ? 0 : (n > 0 ? i: t)(n)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(42)("keys"),
        u = i(20);
        n.exports = function(n) {
            return r[n] || (r[n] = u(n))
        }
    },
    function(n, t) {
        "use strict";
        t.f = {}.propertyIsEnumerable
    },
    function(n, t, i) {
        "use strict";
        var r = i(18),
        u = i(2)("toStringTag"),
        f = r(function() {
            return arguments
        } ()) == "Arguments",
        e = function(n, t) {
            try {
                return n[t]
            } catch(i) {}
        };
        n.exports = function(n) {
            var t, i, o;
            return n === undefined ? "Undefined": n === null ? "Null": typeof(i = e(t = Object(n), u)) == "string" ? i: f ? r(t) : (o = r(t)) == "Object" && typeof t.callee == "function" ? "Arguments": o
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(27);
        n.exports = function(n) {
            return Object(r(n))
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n, t, i, r) {
            if (! (n instanceof t) || r !== undefined && r in n) throw TypeError(i + ": incorrect invocation!");
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        function u(n) {
            var t, i;
            this.promise = new n(function(n, r) {
                if (t !== undefined || i !== undefined) throw TypeError("Bad Promise constructor");
                t = n;
                i = r
            });
            this.resolve = r(t);
            this.reject = r(i)
        }
        var r = i(16);
        n.exports.f = function(n) {
            return new u(n)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(9);
        n.exports = function(n, t, i) {
            for (var u in t) r(n, u, t[u], i);
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        var o = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        },
        r = i(20)("meta"),
        s = i(1),
        f = i(7),
        h = i(15).f,
        c = 0,
        u = Object.isExtensible ||
        function() {
            return ! 0
        },
        l = !i(13)(function() {
            return u(Object.preventExtensions({}))
        }),
        e = function(n) {
            h(n, r, {
                value: {
                    i: "O" + ++c,
                    w: {}
                }
            })
        },
        a = function(n, t) {
            if (!s(n)) return (typeof n == "undefined" ? "undefined": o(n)) == "symbol" ? n: (typeof n == "string" ? "S": "P") + n;
            if (!f(n, r)) {
                if (!u(n)) return "F";
                if (!t) return "E";
                e(n)
            }
            return n[r].i
        },
        v = function(n, t) {
            if (!f(n, r)) {
                if (!u(n)) return ! 0;
                if (!t) return ! 1;
                e(n)
            }
            return n[r].w
        },
        y = function(n) {
            return l && p.NEED && u(n) && !f(n, r) && e(n),
            n
        },
        p = n.exports = {
            KEY: r,
            NEED: !1,
            fastKey: a,
            getWeak: v,
            onFreeze: y
        }
    },
    function(n, t, i) {
        "use strict";
        function u(n) {
            var u = n.getResponseHeader,
            o = n.isHttp,
            f = n.rangeChunkSize,
            s = n.disableRange,
            t, e, i;
            return (r.assert(f > 0), t = {
                allowRangeRequests: !1,
                suggestedLength: undefined
            },
            s || !o) ? t: u("Accept-Ranges") !== "bytes" ? t: (e = u("Content-Encoding") || "identity", e !== "identity") ? t: (i = parseInt(u("Content-Length"), 10), !Number.isInteger(i)) ? t: (t.suggestedLength = i, i <= 2 * f) ? t: (t.allowRangeRequests = !0, t)
        }
        function f(n, t) {
            return n === 404 || n === 0 && /^file:/.test(t) ? new r.MissingPDFException('Missing PDF "' + t + '".') : new r.UnexpectedResponseException("Unexpected server response (" + n + ') while retrieving PDF "' + t + '".', n)
        }
        function e(n) {
            return n === 200 || n === 206
        }
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.validateResponseStatus = t.validateRangeRequestCapabilities = t.createResponseStatusError = undefined;
        var r = i(0);
        t.createResponseStatusError = f;
        t.validateRangeRequestCapabilities = u;
        t.validateResponseStatus = e
    },
    function(n, t, i) {
        "use strict";
        n.exports = !i(12) && !i(13)(function() {
            return Object.defineProperty(i(24)("div"), "a", {
                get: function() {
                    return 7
                }
            }).a != 7
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(1);
        n.exports = function(n, t) {
            if (!r(n)) return n;
            var i, u;
            if (t && typeof(i = n.toString) == "function" && !r(u = i.call(n)) || typeof(i = n.valueOf) == "function" && !r(u = i.call(n)) || !t && typeof(i = n.toString) == "function" && !r(u = i.call(n))) return u;
            throw TypeError("Can't convert object to primitive value");
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(17),
        u = i(28),
        f = i(69);
        n.exports = function(n) {
            return function(t, i, e) {
                var s = r(t),
                h = u(s.length),
                o = f(e, h),
                c;
                if (n && i != i) {
                    while (h > o) if (c = s[o++], c != c) return ! 0
                } else for (; h > o; o++) if ((n || o in s) && s[o] === i) return n || o || 0;
                return ! n && -1
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(3),
        u = "__core-js_shared__",
        f = r[u] || (r[u] = {});
        n.exports = function(n) {
            return f[n] || (f[n] = {})
        }
    },
    function(n) {
        "use strict";
        n.exports = "constructor,hasOwnProperty,isPrototypeOf,propertyIsEnumerable,toLocaleString,toString,valueOf".split(",")
    },
    function(n, t, i) {
        "use strict";
        var r = i(2)("unscopables"),
        u = Array.prototype;
        u[r] == undefined && i(11)(u, r, {});
        n.exports = function(n) {
            u[r][n] = !0
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(32),
        r = {};
        r[i(2)("toStringTag")] = "z";
        r + "" != "[object z]" && i(9)(Object.prototype, "toString",
        function() {
            return "[object " + u(this) + "]"
        },
        !0)
    },
    function(n, t, i) {
        "use strict";
        var o = i(47),
        f = i(4),
        a = i(9),
        s = i(11),
        v = i(7),
        h = i(19),
        y = i(80),
        p = i(22),
        w = i(83),
        r = i(2)("iterator"),
        e = !([].keys && "next" in [].keys()),
        b = "@@iterator",
        c = "keys",
        u = "values",
        l = function() {
            return this
        };
        n.exports = function(n, t, i, k, d, g, nt) {
            y(i, t, k);
            var et = function(n) {
                if (!e && n in tt) return tt[n];
                switch (n) {
                case c:
                    return function() {
                        return new i(this, n)
                    };
                case u:
                    return function() {
                        return new i(this, n)
                    }
                }
                return function() {
                    return new i(this, n)
                }
            },
            ct = t + " Iterator",
            st = d == u,
            ht = !1,
            tt = n.prototype,
            it = tt[r] || tt[b] || d && tt[d],
            rt = it || et(d),
            at = d ? st ? et("entries") : rt: undefined,
            lt = t == "Array" ? tt.entries || it: it,
            ft,
            ot,
            ut;
            if (lt && (ut = w(lt.call(new n)), ut !== Object.prototype && ut.next && (p(ut, ct, !0), o || v(ut, r) || s(ut, r, l))), st && it && it.name !== u && (ht = !0, rt = function() {
                return it.call(this)
            }), (!o || nt) && (e || ht || !tt[r]) && s(tt, r, rt), h[t] = rt, h[ct] = l, d) if (ft = {
                values: st ? rt: et(u),
                keys: g ? rt: et(c),
                entries: at
            },
            nt) for (ot in ft) ot in tt || a(tt, ot, ft[ot]);
            else f(f.P + f.F * (e || ht), t, ft);
            return ft
        }
    },
    function(n) {
        "use strict";
        n.exports = !1
    },
    function(n, t, i) {
        "use strict";
        var r = i(3).document;
        n.exports = r && r.documentElement
    },
    function(n, t, i) {
        "use strict";
        for (var o = i(84), b = i(21), k = i(9), d = i(3), s = i(11), h = i(19), c = i(2), l = c("iterator"), a = c("toStringTag"), v = h.Array, y = {
            CSSRuleList: !0,
            CSSStyleDeclaration: !1,
            CSSValueList: !1,
            ClientRectList: !1,
            DOMRectList: !1,
            DOMStringList: !1,
            DOMTokenList: !0,
            DataTransferItemList: !1,
            FileList: !1,
            HTMLAllCollection: !1,
            HTMLCollection: !1,
            HTMLFormElement: !1,
            HTMLSelectElement: !1,
            MediaList: !0,
            MimeTypeArray: !1,
            NamedNodeMap: !1,
            NodeList: !0,
            PaintRequestList: !1,
            Plugin: !1,
            PluginArray: !1,
            SVGLengthList: !1,
            SVGNumberList: !1,
            SVGPathSegList: !1,
            SVGPointList: !1,
            SVGStringList: !1,
            SVGTransformList: !1,
            SourceBufferList: !1,
            StyleSheetList: !0,
            TextTrackCueList: !1,
            TextTrackList: !1,
            TouchList: !1
        },
        p = b(y), e = 0; e < p.length; e++) {
            var u = p[e],
            g = y[u],
            w = d[u],
            r = w && w.prototype,
            f;
            if (r && (r[l] || s(r, l, v), r[a] || s(r, a, u), h[u] = v, g)) for (f in o) r[f] || k(r, f, o[f], !0)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(6),
        u = i(16),
        f = i(2)("species");
        n.exports = function(n, t) {
            var i = r(n).constructor,
            e;
            return i === undefined || (e = r(i)[f]) == undefined ? t: u(e)
        }
    },
    function(n, t, i) {
        "use strict";
        var o = i(10),
        g = i(90),
        y = i(48),
        p = i(24),
        r = i(3),
        w = r.process,
        s = r.setImmediate,
        h = r.clearImmediate,
        b = r.MessageChannel,
        c = r.Dispatch,
        l = 0,
        f = {},
        k = "onreadystatechange",
        u,
        a,
        v,
        e = function() {
            var n = +this,
            t;
            f.hasOwnProperty(n) && (t = f[n], delete f[n], t())
        },
        d = function(n) {
            e.call(n.data)
        };
        s && h || (s = function(n) {
            for (var t = [], i = 1; arguments.length > i;) t.push(arguments[i++]);
            return f[++l] = function() {
                g(typeof n == "function" ? n: Function(n), t)
            },
            u(l),
            l
        },
        h = function(n) {
            delete f[n]
        },
        i(18)(w) == "process" ? u = function(n) {
            w.nextTick(o(e, n, 1))
        }: c && c.now ? u = function(n) {
            c.now(o(e, n, 1))
        }: b ? (a = new b, v = a.port2, a.port1.onmessage = d, u = o(v.postMessage, v, 1)) : r.addEventListener && typeof postMessage == "function" && !r.importScripts ? (u = function(n) {
            r.postMessage(n + "", "*")
        },
        r.addEventListener("message", d, !1)) : u = k in p("script") ?
        function(n) {
            y.appendChild(p("script"))[k] = function() {
                y.removeChild(this);
                e.call(n)
            }
        }: function(n) {
            setTimeout(o(e, n, 1), 0)
        });
        n.exports = {
            set: s,
            clear: h
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n) {
            try {
                return {
                    e: !1,
                    v: n()
                }
            } catch(t) {
                return {
                    e: !0,
                    v: t
                }
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(6),
        u = i(1),
        f = i(35);
        n.exports = function(n, t) {
            if (r(n), u(t) && t.constructor === n) return t;
            var i = f.f(n),
            e = i.resolve;
            return e(t),
            i.promise
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(2)("iterator"),
        f = !1,
        u;
        try {
            u = [7][r]();
            u["return"] = function() {
                f = !0
            };
            Array.from(u,
            function() {
                throw 2;
            })
        } catch(e) {}
        n.exports = function(n, t) {
            var u, i, e;
            if (!t && !f) return ! 1;
            u = !1;
            try {
                i = [7];
                e = i[r]();
                e.next = function() {
                    return {
                        done: u = !0
                    }
                };
                i[r] = function() {
                    return e
                };
                n(i)
            } catch(o) {}
            return u
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(10),
        u = i(26),
        f = i(33),
        e = i(28),
        o = i(97);
        n.exports = function(n, t) {
            var s = n == 1,
            l = n == 2,
            a = n == 3,
            i = n == 4,
            h = n == 6,
            v = n == 5 || h,
            c = t || o;
            return function(t, o, y) {
                for (var g = f(t), b = u(g), tt = r(o, y, 3), nt = e(b.length), p = 0, k = s ? c(t, nt) : l ? c(t, 0) : undefined, w, d; nt > p; p++) if ((v || p in b) && (w = b[p], d = tt(w, p, g), n)) if (s) k[p] = d;
                else if (d) switch (n) {
                case 3:
                    return ! 0;
                case 5:
                    return w;
                case 6:
                    return p;
                case 2:
                    k.push(w)
                } else if (i) return ! 1;
                return h ? -1 : a || i ? i: k
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(1);
        n.exports = function(n, t) {
            if (!r(n) || n._t !== t) throw TypeError("Incompatible receiver, " + t + " required!");
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        function st(n) {
            return n && n.__esModule ? n: {
                "default": n
            }
        }
        function ht(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        function lt(n) {
            w = n
        }
        function at(n) {
            var s = new yt,
            f, t, e, p, a, v;
            if (typeof n == "string") f = {
                url: n
            };
            else if (r.isArrayBuffer(n)) f = {
                data: n
            };
            else if (n instanceof b) f = {
                range: n
            };
            else {
                if ((typeof n == "undefined" ? "undefined": o(n)) !== "object") throw new Error("Invalid parameter in getDocument, need either Uint8Array, string or a parameter object");
                if (!n.url && !n.data && !n.range) throw new Error("Invalid parameter object: need either .data, .range or .url");
                f = n
            }
            var i = {},
            c = null,
            h = null,
            y = u.DOMCMapReaderFactory;
            for (t in f) {
                if (t === "url" && typeof window != "undefined") {
                    i[t] = new URL(f[t], window.location).href;
                    continue
                } else if (t === "range") {
                    c = f[t];
                    continue
                } else if (t === "worker") {
                    h = f[t];
                    continue
                } else if (t !== "data" || f[t] instanceof Uint8Array) {
                    if (t === "CMapReaderFactory") {
                        y = f[t];
                        continue
                    }
                } else {
                    if (e = f[t], typeof e == "string") i[t] = r.stringToBytes(e);
                    else if ((typeof e == "undefined" ? "undefined": o(e)) !== "object" || e === null || isNaN(e.length)) if (r.isArrayBuffer(e)) i[t] = new Uint8Array(e);
                    else throw new Error("Invalid PDF binary data: either typed array, string or array-like object is expected in the data property.");
                    else i[t] = new Uint8Array(e);
                    continue
                }
                i[t] = f[t]
            }
            return i.rangeChunkSize = i.rangeChunkSize || ct,
            i.ignoreErrors = i.stopAtErrors !== !0,
            p = Object.values(r.NativeImageDecoding),
            i.nativeImageDecoderSupport !== undefined && p.includes(i.nativeImageDecoderSupport) || (i.nativeImageDecoderSupport = r.NativeImageDecoding.DECODE),
            h || (a = u.getDefaultSetting("workerPort"), h = a ? l.fromPort(a) : new l, s._worker = h),
            v = s.docId,
            h.promise.then(function() {
                if (s.destroyed) throw new Error("Loading aborted");
                return vt(h, i, c, v).then(function(n) {
                    var t, f, e;
                    if (s.destroyed) throw new Error("Loading aborted");
                    t = void 0;
                    c ? t = new ot.PDFDataTransportStream(i, c) : i.data || (t = new w({
                        source: i,
                        disableRange: u.getDefaultSetting("disableRange")
                    }));
                    f = new r.MessageHandler(v, n, h.port);
                    f.postMessageTransfers = h.postMessageTransfers;
                    e = new pt(f, s, t, y);
                    s._transport = e;
                    f.send("Ready", null)
                })
            }).
            catch(s._capability.reject),
            s
        }
        function vt(n, t, i, r) {
            if (n.destroyed) return Promise.reject(new Error("Worker was destroyed"));
            return t.disableAutoFetch = u.getDefaultSetting("disableAutoFetch"),
            t.disableStream = u.getDefaultSetting("disableStream"),
            t.chunkedViewerLoading = !!i,
            i && (t.length = i.length, t.initialData = i.initialData),
            n.messageHandler.sendWithPromise("GetDocRequest", {
                docId: r,
                apiVersion: "2.0.104",
                source: {
                    data: t.data,
                    url: t.url,
                    password: t.password,
                    disableAutoFetch: t.disableAutoFetch,
                    rangeChunkSize: t.rangeChunkSize,
                    length: t.length
                },
                maxImageSize: u.getDefaultSetting("maxImageSize"),
                disableFontFace: u.getDefaultSetting("disableFontFace"),
                disableCreateObjectURL: u.getDefaultSetting("disableCreateObjectURL"),
                postMessageTransfers: u.getDefaultSetting("postMessageTransfers") && !h,
                docBaseUrl: t.docBaseUrl,
                nativeImageDecoderSupport: t.nativeImageDecoderSupport,
                ignoreErrors: t.ignoreErrors,
                isEvalSupported: u.getDefaultSetting("isEvalSupported")
            }).then(function(t) {
                if (n.destroyed) throw new Error("Worker was destroyed");
                return t
            })
        }
        var p, w;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.build = t.version = t.setPDFNetworkStreamClass = t.PDFPageProxy = t.PDFDocumentProxy = t.PDFWorker = t.PDFDataRangeTransport = t.LoopbackPort = t.getDocument = undefined;
        var rt = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        o = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        },
        r = i(0),
        u = i(8),
        a = i(114),
        ut = i(115),
        ft = i(14),
        f = st(ft),
        et = i(59),
        ot = i(117);
        var ct = 65536,
        e = !1,
        s, h = !1,
        v = typeof document != "undefined" && document.currentScript ? document.currentScript.src: null,
        y = null,
        c = !1;
        typeof window == "undefined" ? (e = !0, typeof require.ensure == "undefined" && (require.ensure = require("node-ensure")), c = !0) : typeof require != "undefined" && typeof require.ensure == "function" && (c = !0);
        typeof requirejs != "undefined" && requirejs.toUrl && (s = requirejs.toUrl("pdfjs-dist/build/pdf.worker.js"));
        p = typeof requirejs != "undefined" && requirejs.load;
        y = c ?
        function(n) {
            require.ensure([],
            function() {
                var t;
                t = require("./pdf.worker.js");
                n(t.WorkerMessageHandler)
            })
        }: p ?
        function(n) {
            requirejs(["pdfjs-dist/build/pdf.worker"],
            function(t) {
                n(t.WorkerMessageHandler)
            })
        }: null;
        var yt = function() {
            function n() {
                this._capability = r.createPromiseCapability();
                this._transport = null;
                this._worker = null;
                this.docId = "d" + t++;
                this.destroyed = !1;
                this.onPassword = null;
                this.onProgress = null;
                this.onUnsupportedFeature = null
            }
            var t = 0;
            return n.prototype = {
                get promise() {
                    return this._capability.promise
                },
                destroy: function() {
                    var n = this,
                    t;
                    return this.destroyed = !0,
                    t = this._transport ? this._transport.destroy() : Promise.resolve(),
                    t.then(function() {
                        n._transport = null;
                        n._worker && (n._worker.destroy(), n._worker = null)
                    })
                },
                then: function() {
                    return this.promise.then.apply(this.promise, arguments)
                }
            },
            n
        } (),
        b = function() {
            function n(n, t) {
                this.length = n;
                this.initialData = t;
                this._rangeListeners = [];
                this._progressListeners = [];
                this._progressiveReadListeners = [];
                this._readyCapability = r.createPromiseCapability()
            }
            return n.prototype = {
                addRangeListener: function(n) {
                    this._rangeListeners.push(n)
                },
                addProgressListener: function(n) {
                    this._progressListeners.push(n)
                },
                addProgressiveReadListener: function(n) {
                    this._progressiveReadListeners.push(n)
                },
                onDataRange: function(n, t) {
                    for (var r = this._rangeListeners,
                    i = 0,
                    u = r.length; i < u; ++i) r[i](n, t)
                },
                onDataProgress: function(n) {
                    var t = this;
                    this._readyCapability.promise.then(function() {
                        for (var r = t._progressListeners,
                        i = 0,
                        u = r.length; i < u; ++i) r[i](n)
                    })
                },
                onDataProgressiveRead: function(n) {
                    var t = this;
                    this._readyCapability.promise.then(function() {
                        for (var r = t._progressiveReadListeners,
                        i = 0,
                        u = r.length; i < u; ++i) r[i](n)
                    })
                },
                transportReady: function() {
                    this._readyCapability.resolve()
                },
                requestDataRange: function() {
                    throw new Error("Abstract method PDFDataRangeTransport.requestDataRange");
                },
                abort: function() {}
            },
            n
        } (),
        k = function() {
            function n(n, t, i) {
                this.pdfInfo = n;
                this.transport = t;
                this.loadingTask = i
            }
            return n.prototype = {
                get numPages() {
                    return this.pdfInfo.numPages
                },
                get fingerprint() {
                    return this.pdfInfo.fingerprint
                },
                getPage: function(n) {
                    return this.transport.getPage(n)
                },
                getPageIndex: function(n) {
                    return this.transport.getPageIndex(n)
                },
                getDestinations: function() {
                    return this.transport.getDestinations()
                },
                getDestination: function(n) {
                    return this.transport.getDestination(n)
                },
                getPageLabels: function() {
                    return this.transport.getPageLabels()
                },
                getPageMode: function() {
                    return this.transport.getPageMode()
                },
                getAttachments: function() {
                    return this.transport.getAttachments()
                },
                getJavaScript: function() {
                    return this.transport.getJavaScript()
                },
                getOutline: function() {
                    return this.transport.getOutline()
                },
                getMetadata: function() {
                    return this.transport.getMetadata()
                },
                getData: function() {
                    return this.transport.getData()
                },
                getDownloadInfo: function() {
                    return this.transport.downloadInfoCapability.promise
                },
                getStats: function() {
                    return this.transport.getStats()
                },
                cleanup: function() {
                    this.transport.startCleanup()
                },
                destroy: function() {
                    return this.loadingTask.destroy()
                }
            },
            n
        } (),
        d = function() {
            function n(n, t, i) {
                this.pageIndex = n;
                this.pageInfo = t;
                this.transport = i;
                this.stats = new r.StatTimer;
                this.stats.enabled = u.getDefaultSetting("enableStats");
                this.commonObjs = i.commonObjs;
                this.objs = new nt;
                this.cleanupAfterRender = !1;
                this.pendingCleanup = !1;
                this.intentStates = Object.create(null);
                this.destroyed = !1
            }
            return n.prototype = {
                get pageNumber() {
                    return this.pageIndex + 1
                },
                get rotate() {
                    return this.pageInfo.rotate
                },
                get ref() {
                    return this.pageInfo.ref
                },
                get userUnit() {
                    return this.pageInfo.userUnit
                },
                get view() {
                    return this.pageInfo.view
                },
                getViewport: function(n, t) {
                    return arguments.length < 2 && (t = this.rotate),
                    new r.PageViewport(this.view, n, t, 0, 0)
                },
                getAnnotations: function(n) {
                    var t = n && n.intent || null;
                    return this.annotationsPromise && this.annotationsIntent === t || (this.annotationsPromise = this.transport.getAnnotations(this.pageIndex, t), this.annotationsIntent = t),
                    this.annotationsPromise
                },
                render: function(n) {
                    var e = this,
                    o = this.stats,
                    f, h, t, s, i, c;
                    return o.time("Overall"),
                    this.pendingCleanup = !1,
                    f = n.intent === "print" ? "print": "display",
                    h = n.canvasFactory || new u.DOMCanvasFactory,
                    this.intentStates[f] || (this.intentStates[f] = Object.create(null)),
                    t = this.intentStates[f],
                    t.displayReadyCapability || (t.receivingOperatorList = !0, t.displayReadyCapability = r.createPromiseCapability(), t.operatorList = {
                        fnArray: [],
                        argsArray: [],
                        lastChunk: !1
                    },
                    this.stats.time("Page Request"), this.transport.messageHandler.send("RenderPageRequest", {
                        pageIndex: this.pageNumber - 1,
                        intent: f,
                        renderInteractiveForms: n.renderInteractiveForms === !0
                    })),
                    s = function(n) {
                        var r = t.renderTasks.indexOf(i);
                        r >= 0 && t.renderTasks.splice(r, 1);
                        e.cleanupAfterRender && (e.pendingCleanup = !0);
                        e._tryCleanup();
                        n ? i.capability.reject(n) : i.capability.resolve();
                        o.timeEnd("Rendering");
                        o.timeEnd("Overall")
                    },
                    i = new bt(s, n, this.objs, this.commonObjs, t.operatorList, this.pageNumber, h),
                    i.useRequestAnimationFrame = f !== "print",
                    t.renderTasks || (t.renderTasks = []),
                    t.renderTasks.push(i),
                    c = i.task,
                    t.displayReadyCapability.promise.then(function(n) {
                        if (e.pendingCleanup) {
                            s();
                            return
                        }
                        o.time("Rendering");
                        i.initializeGraphics(n);
                        i.operatorListChanged()
                    }).
                    catch(s),
                    c
                },
                getOperatorList: function() {
                    function u() {
                        if (n.operatorList.lastChunk) {
                            n.opListReadCapability.resolve(n.operatorList);
                            var t = n.renderTasks.indexOf(i);
                            t >= 0 && n.renderTasks.splice(t, 1)
                        }
                    }
                    var t = "oplist",
                    n, i;
                    return this.intentStates[t] || (this.intentStates[t] = Object.create(null)),
                    n = this.intentStates[t],
                    n.opListReadCapability || (i = {},
                    i.operatorListChanged = u, n.receivingOperatorList = !0, n.opListReadCapability = r.createPromiseCapability(), n.renderTasks = [], n.renderTasks.push(i), n.operatorList = {
                        fnArray: [],
                        argsArray: [],
                        lastChunk: !1
                    },
                    this.transport.messageHandler.send("RenderPageRequest", {
                        pageIndex: this.pageIndex,
                        intent: t
                    })),
                    n.opListReadCapability.promise
                },
                streamTextContent: function() {
                    var n = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
                    return this.transport.messageHandler.sendWithStream("GetTextContent", {
                        pageIndex: this.pageNumber - 1,
                        normalizeWhitespace: n.normalizeWhitespace === !0,
                        combineTextItems: n.disableCombineTextItems !== !0
                    },
                    {
                        highWaterMark: 100,
                        size: function(n) {
                            return n.items.length
                        }
                    })
                },
                getTextContent: function(n) {
                    n = n || {};
                    var t = this.streamTextContent(n);
                    return new Promise(function(n, i) {
                        function f() {
                            e.read().then(function(t) {
                                var i = t.value,
                                e = t.done;
                                if (e) {
                                    n(u);
                                    return
                                }
                                r.Util.extendObj(u.styles, i.styles);
                                r.Util.appendToArray(u.items, i.items);
                                f()
                            },
                            i)
                        }
                        var e = t.getReader(),
                        u = {
                            items: [],
                            styles: Object.create(null)
                        };
                        f()
                    })
                },
                _destroy: function() {
                    this.destroyed = !0;
                    this.transport.pageCache[this.pageIndex] = null;
                    var n = [];
                    return Object.keys(this.intentStates).forEach(function(t) {
                        if (t !== "oplist") {
                            var i = this.intentStates[t];
                            i.renderTasks.forEach(function(t) {
                                var i = t.capability.promise.
                                catch(function() {});
                                n.push(i);
                                t.cancel()
                            })
                        }
                    },
                    this),
                    this.objs.clear(),
                    this.annotationsPromise = null,
                    this.pendingCleanup = !1,
                    Promise.all(n)
                },
                cleanup: function() {
                    this.pendingCleanup = !0;
                    this._tryCleanup()
                },
                _tryCleanup: function() {
                    this.pendingCleanup && !Object.keys(this.intentStates).some(function(n) {
                        var t = this.intentStates[n];
                        return t.renderTasks.length !== 0 || t.receivingOperatorList
                    },
                    this) && (Object.keys(this.intentStates).forEach(function(n) {
                        delete this.intentStates[n]
                    },
                    this), this.objs.clear(), this.annotationsPromise = null, this.pendingCleanup = !1)
                },
                _startRenderPage: function(n, t) {
                    var i = this.intentStates[t];
                    i.displayReadyCapability && i.displayReadyCapability.resolve(n)
                },
                _renderPageChunk: function(n, t) {
                    for (var r = this.intentStates[t], i = 0, u = n.length; i < u; i++) r.operatorList.fnArray.push(n.fnArray[i]),
                    r.operatorList.argsArray.push(n.argsArray[i]);
                    for (r.operatorList.lastChunk = n.lastChunk, i = 0; i < r.renderTasks.length; i++) r.renderTasks[i].operatorListChanged();
                    n.lastChunk && (r.receivingOperatorList = !1, this._tryCleanup())
                }
            },
            n
        } (),
        g = function() {
            function n(t) {
                ht(this, n);
                this._listeners = [];
                this._defer = t;
                this._deferred = Promise.resolve(undefined)
            }
            return rt(n, [{
                key: "postMessage",
                value: function(n, t) {
                    function f(n) {
                        var u, e, l, c, s, h;
                        if ((typeof n == "undefined" ? "undefined": o(n)) !== "object" || n === null) return n;
                        if (i.has(n)) return i.get(n);
                        if ((e = n.buffer) && r.isArrayBuffer(e)) return l = t && t.indexOf(e) >= 0,
                        u = n === e ? n: l ? new n.constructor(e, n.byteOffset, n.byteLength) : new n.constructor(n),
                        i.set(n, u),
                        u;
                        u = Array.isArray(n) ? [] : {};
                        i.set(n, u);
                        for (c in n) {
                            for (h = n; ! (s = Object.getOwnPropertyDescriptor(h, c));) h = Object.getPrototypeOf(h);
                            typeof s.value != "undefined" && typeof s.value != "function" && (u[c] = f(s.value))
                        }
                        return u
                    }
                    var u = this,
                    i, e;
                    if (!this._defer) {
                        this._listeners.forEach(function(t) {
                            t.call(this, {
                                data: n
                            })
                        },
                        this);
                        return
                    }
                    i = new WeakMap;
                    e = {
                        data: f(n)
                    };
                    this._deferred.then(function() {
                        u._listeners.forEach(function(n) {
                            n.call(this, e)
                        },
                        u)
                    })
                }
            },
            {
                key: "addEventListener",
                value: function(n, t) {
                    this._listeners.push(t)
                }
            },
            {
                key: "removeEventListener",
                value: function(n, t) {
                    var i = this._listeners.indexOf(t);
                    this._listeners.splice(i, 1)
                }
            },
            {
                key: "terminate",
                value: function() {
                    this._listeners = []
                }
            }]),
            n
        } (),
        l = function() {
            function f() {
                if (typeof s != "undefined") return s;
                if (u.getDefaultSetting("workerSrc")) return u.getDefaultSetting("workerSrc");
                if (v) return v.replace(/(\.(?:min\.)?js)(\?.*)?$/i, ".worker$1$2");
                throw new Error("No PDFJS.workerSrc specified");
            }
            function c() {
                var t;
                return n ? n.promise: (n = r.createPromiseCapability(), t = y ||
                function(n) {
                    r.Util.loadScript(f(),
                    function() {
                        n(window.pdfjsDistBuildPdfWorker.WorkerMessageHandler)
                    })
                },
                t(n.resolve), n.promise)
            }
            function l(n) {
                var t = "importScripts('" + n + "');";
                return URL.createObjectURL(new Blob([t]))
            }
            function i(n, i) {
                if (i && t.has(i)) throw new Error("Cannot use more than one PDFWorker per port");
                if (this.name = n, this.destroyed = !1, this.postMessageTransfers = !0, this._readyCapability = r.createPromiseCapability(), this._port = null, this._webWorker = null, this._messageHandler = null, i) {
                    t.set(i, this);
                    this._initializeFromPort(i);
                    return
                }
                this._initialize()
            }
            var o = 0,
            n = void 0,
            t = new WeakMap;
            return i.prototype = {
                get promise() {
                    return this._readyCapability.promise
                },
                get port() {
                    return this._port
                },
                get messageHandler() {
                    return this._messageHandler
                },
                _initializeFromPort: function(n) {
                    this._port = n;
                    this._messageHandler = new r.MessageHandler("main", "worker", n);
                    this._messageHandler.on("ready",
                    function() {});
                    this._readyCapability.resolve()
                },
                _initialize: function() {
                    var n = this,
                    o, a;
                    if (!e && !u.getDefaultSetting("disableWorker") && typeof Worker != "undefined") {
                        o = f();
                        try {
                            r.isSameOrigin(window.location.href, o) || (o = l(new URL(o, window.location).href));
                            var i = new Worker(o),
                            t = new r.MessageHandler("main", "worker", i),
                            c = function() {
                                i.removeEventListener("error", s);
                                t.destroy();
                                i.terminate();
                                n.destroyed ? n._readyCapability.reject(new Error("Worker was destroyed")) : n._setupFakeWorker()
                            },
                            s = function() {
                                n._webWorker || c()
                            };
                            i.addEventListener("error", s);
                            t.on("test",
                            function(u) {
                                if (i.removeEventListener("error", s), n.destroyed) {
                                    c();
                                    return
                                }
                                var f = u && u.supportTypedArray;
                                f ? (n._messageHandler = t, n._port = i, n._webWorker = i, u.supportTransfers || (n.postMessageTransfers = !1, h = !0), n._readyCapability.resolve(), t.send("configure", {
                                    verbosity: r.getVerbosityLevel()
                                })) : (n._setupFakeWorker(), t.destroy(), i.terminate())
                            });
                            t.on("console_log",
                            function(n) {
                                console.log.apply(console, n)
                            });
                            t.on("console_error",
                            function(n) {
                                console.error.apply(console, n)
                            });
                            t.on("ready",
                            function() {
                                if (i.removeEventListener("error", s), n.destroyed) {
                                    c();
                                    return
                                }
                                try {
                                    a()
                                } catch(t) {
                                    n._setupFakeWorker()
                                }
                            });
                            a = function() {
                                var i = u.getDefaultSetting("postMessageTransfers") && !h,
                                n = new Uint8Array([i ? 255 : 0]);
                                try {
                                    t.send("test", n, [n.buffer])
                                } catch(f) {
                                    r.info("Cannot use postMessage transfers");
                                    n[0] = 0;
                                    t.send("test", n)
                                }
                            };
                            a();
                            return
                        } catch(v) {
                            r.info("The worker has been disabled.")
                        }
                    }
                    this._setupFakeWorker()
                },
                _setupFakeWorker: function() {
                    var n = this;
                    e || u.getDefaultSetting("disableWorker") || (r.warn("Setting up fake worker."), e = !0);
                    c().then(function(t) {
                        var f, i, u, e, s;
                        if (n.destroyed) {
                            n._readyCapability.reject(new Error("Worker was destroyed"));
                            return
                        }
                        f = Uint8Array !== Float32Array;
                        i = new g(f);
                        n._port = i;
                        u = "fake" + o++;
                        e = new r.MessageHandler(u + "_worker", u, i);
                        t.setup(e, i);
                        s = new r.MessageHandler(u, u + "_worker", i);
                        n._messageHandler = s;
                        n._readyCapability.resolve()
                    })
                },
                destroy: function() {
                    this.destroyed = !0;
                    this._webWorker && (this._webWorker.terminate(), this._webWorker = null);
                    t.delete(this._port);
                    this._port = null;
                    this._messageHandler && (this._messageHandler.destroy(), this._messageHandler = null)
                }
            },
            i.fromPort = function(n) {
                return t.has(n) ? t.get(n) : new i(null, n)
            },
            i
        } (),
        pt = function() {
            function n(n, t, i, f) {
                this.messageHandler = n;
                this.loadingTask = t;
                this.commonObjs = new nt;
                this.fontLoader = new a.FontLoader(t.docId);
                this.CMapReaderFactory = new f({
                    baseUrl: u.getDefaultSetting("cMapUrl"),
                    isCompressed: u.getDefaultSetting("cMapPacked")
                });
                this.destroyed = !1;
                this.destroyCapability = null;
                this._passwordCapability = null;
                this._networkStream = i;
                this._fullReader = null;
                this._lastProgress = null;
                this.pageCache = [];
                this.pagePromises = [];
                this.downloadInfoCapability = r.createPromiseCapability();
                this.setupMessageHandler()
            }
            return n.prototype = {
                destroy: function() {
                    var n = this,
                    t, i;
                    return this.destroyCapability ? this.destroyCapability.promise: (this.destroyed = !0, this.destroyCapability = r.createPromiseCapability(), this._passwordCapability && this._passwordCapability.reject(new Error("Worker was destroyed during onPassword callback")), t = [], this.pageCache.forEach(function(n) {
                        n && t.push(n._destroy())
                    }), this.pageCache = [], this.pagePromises = [], i = this.messageHandler.sendWithPromise("Terminate", null), t.push(i), Promise.all(t).then(function() {
                        n.fontLoader.clear();
                        n._networkStream && n._networkStream.cancelAllRequests();
                        n.messageHandler && (n.messageHandler.destroy(), n.messageHandler = null);
                        n.destroyCapability.resolve()
                    },
                    this.destroyCapability.reject), this.destroyCapability.promise)
                },
                setupMessageHandler: function() {
                    var n = this.messageHandler,
                    t = this.loadingTask;
                    n.on("GetReader",
                    function(n, t) {
                        var i = this;
                        r.assert(this._networkStream);
                        this._fullReader = this._networkStream.getFullReader();
                        this._fullReader.onProgress = function(n) {
                            i._lastProgress = {
                                loaded: n.loaded,
                                total: n.total
                            }
                        };
                        t.onPull = function() {
                            i._fullReader.read().then(function(n) {
                                var i = n.value,
                                u = n.done;
                                if (u) {
                                    t.close();
                                    return
                                }
                                r.assert(r.isArrayBuffer(i));
                                t.enqueue(new Uint8Array(i), 1, [i])
                            }).
                            catch(function(n) {
                                t.error(n)
                            })
                        };
                        t.onCancel = function(n) {
                            i._fullReader.cancel(n)
                        }
                    },
                    this);
                    n.on("ReaderHeadersReady",
                    function() {
                        var t = this,
                        i = r.createPromiseCapability(),
                        n = this._fullReader;
                        return n.headersReady.then(function() {
                            if (!n.isStreamingSupported || !n.isRangeSupported) {
                                if (t._lastProgress) {
                                    var r = t.loadingTask;
                                    if (r.onProgress) r.onProgress(t._lastProgress)
                                }
                                n.onProgress = function(n) {
                                    var i = t.loadingTask;
                                    if (i.onProgress) i.onProgress({
                                        loaded: n.loaded,
                                        total: n.total
                                    })
                                }
                            }
                            i.resolve({
                                isStreamingSupported: n.isStreamingSupported,
                                isRangeSupported: n.isRangeSupported,
                                contentLength: n.contentLength
                            })
                        },
                        i.reject),
                        i.promise
                    },
                    this);
                    n.on("GetRangeReader",
                    function(n, t) {
                        r.assert(this._networkStream);
                        var i = this._networkStream.getRangeReader(n.begin, n.end);
                        t.onPull = function() {
                            i.read().then(function(n) {
                                var i = n.value,
                                u = n.done;
                                if (u) {
                                    t.close();
                                    return
                                }
                                r.assert(r.isArrayBuffer(i));
                                t.enqueue(new Uint8Array(i), 1, [i])
                            }).
                            catch(function(n) {
                                t.error(n)
                            })
                        };
                        t.onCancel = function(n) {
                            i.cancel(n)
                        }
                    },
                    this);
                    n.on("GetDoc",
                    function(n) {
                        var r = n.pdfInfo,
                        t, i;
                        this.numPages = n.pdfInfo.numPages;
                        t = this.loadingTask;
                        i = new k(r, this, t);
                        this.pdfDocument = i;
                        t._capability.resolve(i)
                    },
                    this);
                    n.on("PasswordRequest",
                    function(n) {
                        var u = this,
                        i;
                        if (this._passwordCapability = r.createPromiseCapability(), t.onPassword) {
                            i = function(n) {
                                u._passwordCapability.resolve({
                                    password: n
                                })
                            };
                            t.onPassword(i, n.code)
                        } else this._passwordCapability.reject(new r.PasswordException(n.message, n.code));
                        return this._passwordCapability.promise
                    },
                    this);
                    n.on("PasswordException",
                    function(n) {
                        t._capability.reject(new r.PasswordException(n.message, n.code))
                    },
                    this);
                    n.on("InvalidPDF",
                    function(n) {
                        this.loadingTask._capability.reject(new r.InvalidPDFException(n.message))
                    },
                    this);
                    n.on("MissingPDF",
                    function(n) {
                        this.loadingTask._capability.reject(new r.MissingPDFException(n.message))
                    },
                    this);
                    n.on("UnexpectedResponse",
                    function(n) {
                        this.loadingTask._capability.reject(new r.UnexpectedResponseException(n.message, n.status))
                    },
                    this);
                    n.on("UnknownError",
                    function(n) {
                        this.loadingTask._capability.reject(new r.UnknownErrorException(n.message, n.details))
                    },
                    this);
                    n.on("DataLoaded",
                    function(n) {
                        this.downloadInfoCapability.resolve(n)
                    },
                    this);
                    n.on("PDFManagerReady",
                    function() {},
                    this);
                    n.on("StartRenderPage",
                    function(n) {
                        if (!this.destroyed) {
                            var t = this.pageCache[n.pageIndex];
                            t.stats.timeEnd("Page Request");
                            t._startRenderPage(n.transparency, n.intent)
                        }
                    },
                    this);
                    n.on("RenderPageChunk",
                    function(n) {
                        if (!this.destroyed) {
                            var t = this.pageCache[n.pageIndex];
                            t._renderPageChunk(n.operatorList, n.intent)
                        }
                    },
                    this);
                    n.on("commonobj",
                    function(n) {
                        var l = this,
                        t, e, i, o, s, h, c;
                        if (!this.destroyed && (t = n[0], e = n[1], !this.commonObjs.hasData(t))) switch (e) {
                        case "Font":
                            if (i = n[2], "error" in i) {
                                o = i.error;
                                r.warn("Error during font loading: " + o);
                                this.commonObjs.resolve(t, o);
                                break
                            }
                            s = null;
                            u.getDefaultSetting("pdfBug") && f.
                        default.FontInspector && f.
                        default.FontInspector.enabled && (s = {
                                registerFont: function(n, t) {
                                    f.
                                default.FontInspector.fontAdded(n, t)
                                }
                            });
                            h = new a.FontFaceObject(i, {
                                isEvalSupported: u.getDefaultSetting("isEvalSupported"),
                                disableFontFace: u.getDefaultSetting("disableFontFace"),
                                fontRegistry: s
                            });
                            c = function() {
                                l.commonObjs.resolve(t, h)
                            };
                            this.fontLoader.bind([h], c);
                            break;
                        case "FontPath":
                            this.commonObjs.resolve(t, n[2]);
                            break;
                        default:
                            throw new Error("Got unknown common object type " + e);
                        }
                    },
                    this);
                    n.on("obj",
                    function(n) {
                        var e;
                        if (!this.destroyed) {
                            var u = n[0],
                            o = n[1],
                            f = n[2],
                            i = this.pageCache[o],
                            t;
                            if (!i.objs.hasData(u)) switch (f) {
                            case "JpegStream":
                                t = n[3];
                                r.loadJpegStream(u, t, i.objs);
                                break;
                            case "Image":
                                t = n[3];
                                i.objs.resolve(u, t);
                                e = 8e6;
                                t && "data" in t && t.data.length > e && (i.cleanupAfterRender = !0);
                                break;
                            default:
                                throw new Error("Got unknown object type " + f);
                            }
                        }
                    },
                    this);
                    n.on("DocProgress",
                    function(n) {
                        if (!this.destroyed) {
                            var t = this.loadingTask;
                            if (t.onProgress) t.onProgress({
                                loaded: n.loaded,
                                total: n.total
                            })
                        }
                    },
                    this);
                    n.on("PageError",
                    function(n) {
                        var r, t, i;
                        if (!this.destroyed) {
                            if (r = this.pageCache[n.pageNum - 1], t = r.intentStates[n.intent], t.displayReadyCapability) t.displayReadyCapability.reject(n.error);
                            else throw new Error(n.error);
                            if (t.operatorList) for (t.operatorList.lastChunk = !0, i = 0; i < t.renderTasks.length; i++) t.renderTasks[i].operatorListChanged()
                        }
                    },
                    this);
                    n.on("UnsupportedFeature",
                    function(n) {
                        if (!this.destroyed) {
                            var t = this.loadingTask;
                            if (t.onUnsupportedFeature) t.onUnsupportedFeature(n.featureId)
                        }
                    },
                    this);
                    n.on("JpegDecode",
                    function(n) {
                        if (this.destroyed) return Promise.reject(new Error("Worker was destroyed"));
                        if (typeof document == "undefined") return Promise.reject(new Error('"document" is not defined.'));
                        var i = n[0],
                        t = n[1];
                        return t !== 3 && t !== 1 ? Promise.reject(new Error("Only 3 components or 1 component can be returned")) : new Promise(function(n, r) {
                            var u = new Image;
                            u.onload = function() {
                                var o = u.width,
                                s = u.height,
                                l = o * s,
                                a = l * 4,
                                f = new Uint8Array(l * t),
                                h = document.createElement("canvas"),
                                c,
                                e,
                                i,
                                r;
                                if (h.width = o, h.height = s, c = h.getContext("2d"), c.drawImage(u, 0, 0), e = c.getImageData(0, 0, o, s).data, t === 3) for (i = 0, r = 0; i < a; i += 4, r += 3) f[r] = e[i],
                                f[r + 1] = e[i + 1],
                                f[r + 2] = e[i + 2];
                                else if (t === 1) for (i = 0, r = 0; i < a; i += 4, r++) f[r] = e[i];
                                n({
                                    data: f,
                                    width: o,
                                    height: s
                                })
                            };
                            u.onerror = function() {
                                r(new Error("JpegDecode failed to load image"))
                            };
                            u.src = i
                        })
                    },
                    this);
                    n.on("FetchBuiltInCMap",
                    function(n) {
                        return this.destroyed ? Promise.reject(new Error("Worker was destroyed")) : this.CMapReaderFactory.fetch({
                            name: n.name
                        })
                    },
                    this)
                },
                getData: function() {
                    return this.messageHandler.sendWithPromise("GetData", null)
                },
                getPage: function(n) {
                    var i = this,
                    t, r;
                    return ! Number.isInteger(n) || n <= 0 || n > this.numPages ? Promise.reject(new Error("Invalid page request")) : (t = n - 1, t in this.pagePromises) ? this.pagePromises[t] : (r = this.messageHandler.sendWithPromise("GetPage", {
                        pageIndex: t
                    }).then(function(n) {
                        if (i.destroyed) throw new Error("Transport destroyed");
                        var r = new d(t, n, i);
                        return i.pageCache[t] = r,
                        r
                    }), this.pagePromises[t] = r, r)
                },
                getPageIndex: function(n) {
                    return this.messageHandler.sendWithPromise("GetPageIndex", {
                        ref: n
                    }).
                    catch(function(n) {
                        return Promise.reject(new Error(n))
                    })
                },
                getAnnotations: function(n, t) {
                    return this.messageHandler.sendWithPromise("GetAnnotations", {
                        pageIndex: n,
                        intent: t
                    })
                },
                getDestinations: function() {
                    return this.messageHandler.sendWithPromise("GetDestinations", null)
                },
                getDestination: function(n) {
                    return this.messageHandler.sendWithPromise("GetDestination", {
                        id: n
                    })
                },
                getPageLabels: function() {
                    return this.messageHandler.sendWithPromise("GetPageLabels", null)
                },
                getPageMode: function() {
                    return this.messageHandler.sendWithPromise("GetPageMode", null)
                },
                getAttachments: function() {
                    return this.messageHandler.sendWithPromise("GetAttachments", null)
                },
                getJavaScript: function() {
                    return this.messageHandler.sendWithPromise("GetJavaScript", null)
                },
                getOutline: function() {
                    return this.messageHandler.sendWithPromise("GetOutline", null)
                },
                getMetadata: function() {
                    return this.messageHandler.sendWithPromise("GetMetadata", null).then(function(n) {
                        return {
                            info: n[0],
                            metadata: n[1] ? new et.Metadata(n[1]) : null
                        }
                    })
                },
                getStats: function() {
                    return this.messageHandler.sendWithPromise("GetStats", null)
                },
                startCleanup: function() {
                    var n = this;
                    this.messageHandler.sendWithPromise("Cleanup", null).then(function() {
                        for (var i, t = 0,
                        r = n.pageCache.length; t < r; t++) i = n.pageCache[t],
                        i && i.cleanup();
                        n.commonObjs.clear();
                        n.fontLoader.clear()
                    })
                }
            },
            n
        } (),
        nt = function() {
            function n() {
                this.objs = Object.create(null)
            }
            return n.prototype = {
                ensureObj: function(n) {
                    if (this.objs[n]) return this.objs[n];
                    var t = {
                        capability: r.createPromiseCapability(),
                        data: null,
                        resolved: !1
                    };
                    return this.objs[n] = t,
                    t
                },
                get: function(n, t) {
                    if (t) return this.ensureObj(n).capability.promise.then(t),
                    null;
                    var i = this.objs[n];
                    if (!i || !i.resolved) throw new Error("Requesting object that isn't resolved yet " + n);
                    return i.data
                },
                resolve: function(n, t) {
                    var i = this.ensureObj(n);
                    i.resolved = !0;
                    i.data = t;
                    i.capability.resolve(t)
                },
                isResolved: function(n) {
                    var t = this.objs;
                    return t[n] ? t[n].resolved: !1
                },
                hasData: function(n) {
                    return this.isResolved(n)
                },
                getData: function(n) {
                    var t = this.objs;
                    return ! t[n] || !t[n].resolved ? null: t[n].data
                },
                clear: function() {
                    this.objs = Object.create(null)
                }
            },
            n
        } (),
        wt = function() {
            function n(n) {
                this._internalRenderTask = n;
                this.onContinue = null
            }
            return n.prototype = {
                get promise() {
                    return this._internalRenderTask.capability.promise
                },
                cancel: function() {
                    this._internalRenderTask.cancel()
                },
                then: function() {
                    return this.promise.then.apply(this.promise, arguments)
                }
            },
            n
        } (),
        bt = function() {
            function t(n, t, i, u, f, e, o) {
                this.callback = n;
                this.params = t;
                this.objs = i;
                this.commonObjs = u;
                this.operatorListIdx = null;
                this.operatorList = f;
                this.pageNumber = e;
                this.canvasFactory = o;
                this.running = !1;
                this.graphicsReadyCallback = null;
                this.graphicsReady = !1;
                this.useRequestAnimationFrame = !1;
                this.cancelled = !1;
                this.capability = r.createPromiseCapability();
                this.task = new wt(this);
                this._continueBound = this._continue.bind(this);
                this._scheduleNextBound = this._scheduleNext.bind(this);
                this._nextBound = this._next.bind(this);
                this._canvas = t.canvasContext.canvas
            }
            var n = new WeakMap;
            return t.prototype = {
                initializeGraphics: function(t) {
                    if (this._canvas) {
                        if (n.has(this._canvas)) throw new Error("Cannot use the same canvas during multiple render() operations. Use different canvas or ensure previous operations were cancelled or completed.");
                        n.set(this._canvas, this)
                    }
                    if (!this.cancelled) {
                        u.getDefaultSetting("pdfBug") && f.
                    default.StepperManager && f.
                    default.StepperManager.enabled && (this.stepper = f.
                    default.StepperManager.create(this.pageNumber - 1), this.stepper.init(this.operatorList), this.stepper.nextBreakPoint = this.stepper.getNextBreakPoint());
                        var i = this.params;
                        this.gfx = new ut.CanvasGraphics(i.canvasContext, this.commonObjs, this.objs, this.canvasFactory, i.imageLayer);
                        this.gfx.beginDrawing({
                            transform: i.transform,
                            viewport: i.viewport,
                            transparency: t,
                            background: i.background
                        });
                        this.operatorListIdx = 0;
                        this.graphicsReady = !0;
                        this.graphicsReadyCallback && this.graphicsReadyCallback()
                    }
                },
                cancel: function() {
                    this.running = !1;
                    this.cancelled = !0;
                    this._canvas && n.delete(this._canvas);
                    this.callback(new u.RenderingCancelledException("Rendering cancelled, page " + this.pageNumber, "canvas"))
                },
                operatorListChanged: function() {
                    if (!this.graphicsReady) {
                        this.graphicsReadyCallback || (this.graphicsReadyCallback = this._continueBound);
                        return
                    } (this.stepper && this.stepper.updateOperatorList(this.operatorList), this.running) || this._continue()
                },
                _continue: function() {
                    if (this.running = !0, !this.cancelled) if (this.task.onContinue) this.task.onContinue(this._scheduleNextBound);
                    else this._scheduleNext()
                },
                _scheduleNext: function() {
                    this.useRequestAnimationFrame && typeof window != "undefined" ? window.requestAnimationFrame(this._nextBound) : Promise.resolve(undefined).then(this._nextBound)
                },
                _next: function() {
                    this.cancelled || (this.operatorListIdx = this.gfx.executeOperatorList(this.operatorList, this.operatorListIdx, this._continueBound, this.stepper), this.operatorListIdx === this.operatorList.argsArray.length && (this.running = !1, this.operatorList.lastChunk && (this.gfx.endDrawing(), this._canvas && n.delete(this._canvas), this.callback())))
                }
            },
            t
        } (),
        tt,
        it;
        t.version = tt = "2.0.104";
        t.build = it = "012d0756";
        t.getDocument = at;
        t.LoopbackPort = g;
        t.PDFDataRangeTransport = b;
        t.PDFWorker = l;
        t.PDFDocumentProxy = k;
        t.PDFPageProxy = d;
        t.setPDFNetworkStreamClass = lt;
        t.version = tt;
        t.build = it
    },
    function(n, t, i) {
        "use strict";
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.WebGLUtils = undefined;
        var r = i(8),
        u = i(0),
        f = function() {
            function o(n, t, i) {
                var r = n.createShader(i),
                u,
                f;
                if (n.shaderSource(r, t), n.compileShader(r), u = n.getShaderParameter(r, n.COMPILE_STATUS), !u) {
                    f = n.getShaderInfoLog(r);
                    throw new Error("Error during shader compilation: " + f);
                }
                return r
            }
            function s(n, t) {
                return o(n, t, n.VERTEX_SHADER)
            }
            function h(n, t) {
                return o(n, t, n.FRAGMENT_SHADER)
            }
            function c(n, t) {
                for (var u, f, i = n.createProgram(), r = 0, e = t.length; r < e; ++r) n.attachShader(i, t[r]);
                if (n.linkProgram(i), u = n.getProgramParameter(i, n.LINK_STATUS), !u) {
                    f = n.getProgramInfoLog(i);
                    throw new Error("Error during program linking: " + f);
                }
                return i
            }
            function l(n, t, i) {
                n.activeTexture(i);
                var r = n.createTexture();
                return n.bindTexture(n.TEXTURE_2D, r),
                n.texParameteri(n.TEXTURE_2D, n.TEXTURE_WRAP_S, n.CLAMP_TO_EDGE),
                n.texParameteri(n.TEXTURE_2D, n.TEXTURE_WRAP_T, n.CLAMP_TO_EDGE),
                n.texParameteri(n.TEXTURE_2D, n.TEXTURE_MIN_FILTER, n.NEAREST),
                n.texParameteri(n.TEXTURE_2D, n.TEXTURE_MAG_FILTER, n.NEAREST),
                n.texImage2D(n.TEXTURE_2D, 0, n.RGBA, n.RGBA, n.UNSIGNED_BYTE, t),
                r
            }
            function e() {
                i || (f = document.createElement("canvas"), i = f.getContext("webgl", {
                    premultipliedalpha: !1
                }))
            }
            function y() {
                var o, t, u;
                e();
                o = f;
                f = null;
                t = i;
                i = null;
                var y = s(t, a),
                p = h(t, v),
                r = c(t, [y, p]);
                t.useProgram(r);
                u = {};
                u.gl = t;
                u.canvas = o;
                u.resolutionLocation = t.getUniformLocation(r, "u_resolution");
                u.positionLocation = t.getAttribLocation(r, "a_position");
                u.backdropLocation = t.getUniformLocation(r, "u_backdrop");
                u.subtypeLocation = t.getUniformLocation(r, "u_subtype");
                var l = t.getAttribLocation(r, "a_texCoord"),
                w = t.getUniformLocation(r, "u_image"),
                b = t.getUniformLocation(r, "u_mask"),
                k = t.createBuffer();
                t.bindBuffer(t.ARRAY_BUFFER, k);
                t.bufferData(t.ARRAY_BUFFER, new Float32Array([0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1]), t.STATIC_DRAW);
                t.enableVertexAttribArray(l);
                t.vertexAttribPointer(l, 2, t.FLOAT, !1, 0, 0);
                t.uniform1i(w, 0);
                t.uniform1i(b, 1);
                n = u
            }
            function p(t, i, r) {
                var e = t.width,
                o = t.height;
                n || y();
                var f = n,
                s = f.canvas,
                u = f.gl;
                s.width = e;
                s.height = o;
                u.viewport(0, 0, u.drawingBufferWidth, u.drawingBufferHeight);
                u.uniform2f(f.resolutionLocation, e, o);
                r.backdrop ? u.uniform4f(f.resolutionLocation, r.backdrop[0], r.backdrop[1], r.backdrop[2], 1) : u.uniform4f(f.resolutionLocation, 0, 0, 0, 0);
                u.uniform1i(f.subtypeLocation, r.subtype === "Luminosity" ? 1 : 0);
                var c = l(u, t, u.TEXTURE0),
                a = l(u, i, u.TEXTURE1),
                h = u.createBuffer();
                return u.bindBuffer(u.ARRAY_BUFFER, h),
                u.bufferData(u.ARRAY_BUFFER, new Float32Array([0, 0, e, 0, 0, o, 0, o, e, 0, e, o]), u.STATIC_DRAW),
                u.enableVertexAttribArray(f.positionLocation),
                u.vertexAttribPointer(f.positionLocation, 2, u.FLOAT, !1, 0, 0),
                u.clearColor(0, 0, 0, 0),
                u.enable(u.BLEND),
                u.blendFunc(u.ONE, u.ONE_MINUS_SRC_ALPHA),
                u.clear(u.COLOR_BUFFER_BIT),
                u.drawArrays(u.TRIANGLES, 0, 6),
                u.flush(),
                u.deleteTexture(c),
                u.deleteTexture(a),
                u.deleteBuffer(h),
                s
            }
            function k() {
                var o, n, r;
                e();
                o = f;
                f = null;
                n = i;
                i = null;
                var l = s(n, w),
                a = h(n, b),
                u = c(n, [l, a]);
                n.useProgram(u);
                r = {};
                r.gl = n;
                r.canvas = o;
                r.resolutionLocation = n.getUniformLocation(u, "u_resolution");
                r.scaleLocation = n.getUniformLocation(u, "u_scale");
                r.offsetLocation = n.getUniformLocation(u, "u_offset");
                r.positionLocation = n.getAttribLocation(u, "a_position");
                r.colorLocation = n.getAttribLocation(u, "a_color");
                t = r
            }
            function d(n, i, r, u, f) {
                var nt, p, tt, it, y, ut, h, et, g, ht, ot, st;
                t || k();
                var d = t,
                ft = d.canvas,
                e = d.gl;
                for (ft.width = n, ft.height = i, e.viewport(0, 0, e.drawingBufferWidth, e.drawingBufferHeight), e.uniform2f(d.resolutionLocation, n, i), nt = 0, p = 0, tt = u.length; p < tt; p++) switch (u[p].type) {
                case "lattice":
                    it = u[p].coords.length / u[p].verticesPerRow | 0;
                    nt += (it - 1) * (u[p].verticesPerRow - 1) * 6;
                    break;
                case "triangles":
                    nt += u[p].coords.length
                }
                var l = new Float32Array(nt * 2),
                s = new Uint8Array(nt * 3),
                b = f.coords,
                a = f.colors,
                c = 0,
                o = 0;
                for (p = 0, tt = u.length; p < tt; p++) {
                    var rt = u[p],
                    w = rt.coords,
                    v = rt.colors;
                    switch (rt.type) {
                    case "lattice":
                        for (y = rt.verticesPerRow, it = w.length / y | 0, ut = 1; ut < it; ut++) for (h = ut * y + 1, et = 1; et < y; et++, h++) l[c] = b[w[h - y - 1]],
                        l[c + 1] = b[w[h - y - 1] + 1],
                        l[c + 2] = b[w[h - y]],
                        l[c + 3] = b[w[h - y] + 1],
                        l[c + 4] = b[w[h - 1]],
                        l[c + 5] = b[w[h - 1] + 1],
                        s[o] = a[v[h - y - 1]],
                        s[o + 1] = a[v[h - y - 1] + 1],
                        s[o + 2] = a[v[h - y - 1] + 2],
                        s[o + 3] = a[v[h - y]],
                        s[o + 4] = a[v[h - y] + 1],
                        s[o + 5] = a[v[h - y] + 2],
                        s[o + 6] = a[v[h - 1]],
                        s[o + 7] = a[v[h - 1] + 1],
                        s[o + 8] = a[v[h - 1] + 2],
                        l[c + 6] = l[c + 2],
                        l[c + 7] = l[c + 3],
                        l[c + 8] = l[c + 4],
                        l[c + 9] = l[c + 5],
                        l[c + 10] = b[w[h]],
                        l[c + 11] = b[w[h] + 1],
                        s[o + 9] = s[o + 3],
                        s[o + 10] = s[o + 4],
                        s[o + 11] = s[o + 5],
                        s[o + 12] = s[o + 6],
                        s[o + 13] = s[o + 7],
                        s[o + 14] = s[o + 8],
                        s[o + 15] = a[v[h]],
                        s[o + 16] = a[v[h] + 1],
                        s[o + 17] = a[v[h] + 2],
                        c += 12,
                        o += 18;
                        break;
                    case "triangles":
                        for (g = 0, ht = w.length; g < ht; g++) l[c] = b[w[g]],
                        l[c + 1] = b[w[g] + 1],
                        s[o] = a[v[g]],
                        s[o + 1] = a[v[g] + 1],
                        s[o + 2] = a[v[g] + 2],
                        c += 2,
                        o += 3
                    }
                }
                return r ? e.clearColor(r[0] / 255, r[1] / 255, r[2] / 255, 1) : e.clearColor(0, 0, 0, 0),
                e.clear(e.COLOR_BUFFER_BIT),
                ot = e.createBuffer(),
                e.bindBuffer(e.ARRAY_BUFFER, ot),
                e.bufferData(e.ARRAY_BUFFER, l, e.STATIC_DRAW),
                e.enableVertexAttribArray(d.positionLocation),
                e.vertexAttribPointer(d.positionLocation, 2, e.FLOAT, !1, 0, 0),
                st = e.createBuffer(),
                e.bindBuffer(e.ARRAY_BUFFER, st),
                e.bufferData(e.ARRAY_BUFFER, s, e.STATIC_DRAW),
                e.enableVertexAttribArray(d.colorLocation),
                e.vertexAttribPointer(d.colorLocation, 3, e.UNSIGNED_BYTE, !1, 0, 0),
                e.uniform2f(d.scaleLocation, f.scaleX, f.scaleY),
                e.uniform2f(d.offsetLocation, f.offsetX, f.offsetY),
                e.drawArrays(e.TRIANGLES, 0, nt),
                e.flush(),
                e.deleteBuffer(ot),
                e.deleteBuffer(st),
                ft
            }
            function g() {
                n && n.canvas && (n.canvas.width = 0, n.canvas.height = 0);
                t && t.canvas && (t.canvas.width = 0, t.canvas.height = 0);
                n = null;
                t = null
            }
            var i, f, a = "  attribute vec2 a_position;                                      attribute vec2 a_texCoord;                                                                                                      uniform vec2 u_resolution;                                                                                                      varying vec2 v_texCoord;                                                                                                        void main() {                                                     vec2 clipSpace = (a_position / u_resolution) * 2.0 - 1.0;       gl_Position = vec4(clipSpace * vec2(1, -1), 0, 1);                                                                              v_texCoord = a_texCoord;                                      }                                                             ",
            v = "  precision mediump float;                                                                                                        uniform vec4 u_backdrop;                                        uniform int u_subtype;                                          uniform sampler2D u_image;                                      uniform sampler2D u_mask;                                                                                                       varying vec2 v_texCoord;                                                                                                        void main() {                                                     vec4 imageColor = texture2D(u_image, v_texCoord);               vec4 maskColor = texture2D(u_mask, v_texCoord);                 if (u_backdrop.a > 0.0) {                                         maskColor.rgb = maskColor.rgb * maskColor.a +                                   u_backdrop.rgb * (1.0 - maskColor.a);         }                                                               float lum;                                                      if (u_subtype == 0) {                                             lum = maskColor.a;                                            } else {                                                          lum = maskColor.r * 0.3 + maskColor.g * 0.59 +                        maskColor.b * 0.11;                                     }                                                               imageColor.a *= lum;                                            imageColor.rgb *= imageColor.a;                                 gl_FragColor = imageColor;                                    }                                                             ",
            n = null,
            w = "  attribute vec2 a_position;                                      attribute vec3 a_color;                                                                                                         uniform vec2 u_resolution;                                      uniform vec2 u_scale;                                           uniform vec2 u_offset;                                                                                                          varying vec4 v_color;                                                                                                           void main() {                                                     vec2 position = (a_position + u_offset) * u_scale;              vec2 clipSpace = (position / u_resolution) * 2.0 - 1.0;         gl_Position = vec4(clipSpace * vec2(1, -1), 0, 1);                                                                              v_color = vec4(a_color / 255.0, 1.0);                         }                                                             ",
            b = "  precision mediump float;                                                                                                        varying vec4 v_color;                                                                                                           void main() {                                                     gl_FragColor = v_color;                                       }                                                             ",
            t = null;
            return {
                get isEnabled() {
                    if (r.getDefaultSetting("disableWebGL")) return ! 1;
                    var n = !1;
                    try {
                        e();
                        n = !!i
                    } catch(t) {}
                    return u.shadow(this, "isEnabled", n)
                },
                composeSMask: p,
                drawFigures: d,
                clear: g
            }
        } ();
        t.WebGLUtils = f
    },
    function(n, t, i) {
        "use strict";
        function o(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        var r;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.Metadata = undefined;
        var u = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        f = i(0),
        e = i(8);
        r = function() {
            function n(t) {
                o(this, n);
                f.assert(typeof t == "string", "Metadata: input is not a string");
                t = this._repair(t);
                var i = new e.SimpleXMLParser;
                t = i.parseFromString(t);
                this._metadata = Object.create(null);
                this._parse(t)
            }
            return u(n, [{
                key: "_repair",
                value: function(n) {
                    return n.replace(/>\\376\\377([^<]+)/g,
                    function(n, t) {
                        for (var i, u = t.replace(/\\([0-3])([0-7])([0-7])/g,
                        function(n, t, i, r) {
                            return String.fromCharCode(t * 64 + i * 8 + r * 1)
                        }), f = "", r = 0, e = u.length; r < e; r += 2) i = u.charCodeAt(r) * 256 + u.charCodeAt(r + 1),
                        f += i >= 32 && i < 127 && i !== 60 && i !== 62 && i !== 38 ? String.fromCharCode(i) : "&#x" + (65536 + i).toString(16).substring(1) + ";";
                        return ">" + f
                    })
                }
            },
            {
                key: "_parse",
                value: function(n) {
                    var t = n.documentElement,
                    o, f, u, s, i, r, h, e, c;
                    if (t.nodeName.toLowerCase() !== "rdf:rdf") for (t = t.firstChild; t && t.nodeName.toLowerCase() !== "rdf:rdf";) t = t.nextSibling;
                    if (o = t ? t.nodeName.toLowerCase() : null, t && o === "rdf:rdf" && t.hasChildNodes()) for (f = t.childNodes, u = 0, s = f.length; u < s; u++) if (i = f[u], i.nodeName.toLowerCase() === "rdf:description") for (r = 0, h = i.childNodes.length; r < h; r++) i.childNodes[r].nodeName.toLowerCase() !== "#text" && (e = i.childNodes[r], c = e.nodeName.toLowerCase(), this._metadata[c] = e.textContent.trim())
                }
            },
            {
                key: "get",
                value: function(n) {
                    return this._metadata[n] || null
                }
            },
            {
                key: "getAll",
                value: function() {
                    return this._metadata
                }
            },
            {
                key: "has",
                value: function(n) {
                    return typeof this._metadata[n] != "undefined"
                }
            }]),
            n
        } ();
        t.Metadata = r
    },
    function(n, t, i) {
        "use strict";
        function e(n, t) {
            if (!n) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
            return t && (typeof t == "object" || typeof t == "function") ? t: n
        }
        function o(n, t) {
            if (typeof t != "function" && t !== null) throw new TypeError("Super expression must either be null or a function, not " + typeof t);
            n.prototype = Object.create(t && t.prototype, {
                constructor: {
                    value: n,
                    enumerable: !1,
                    writable: !0,
                    configurable: !0
                }
            });
            t && (Object.setPrototypeOf ? Object.setPrototypeOf(n, t) : n.__proto__ = t)
        }
        function u(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.AnnotationLayer = undefined;
        var f = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        h = i(8),
        r = i(0);
        var v = function() {
            function n() {
                u(this, n)
            }
            return f(n, null, [{
                key: "create",
                value: function(n) {
                    var i = n.data.annotationType,
                    t;
                    switch (i) {
                    case r.AnnotationType.LINK:
                        return new y(n);
                    case r.AnnotationType.TEXT:
                        return new p(n);
                    case r.AnnotationType.WIDGET:
                        t = n.data.fieldType;
                        switch (t) {
                        case "Tx":
                            return new w(n);
                        case "Btn":
                            if (n.data.radioButton) return new k(n);
                            if (n.data.checkBox) return new b(n);
                            r.warn("Unimplemented button widget annotation: pushbutton");
                            break;
                        case "Ch":
                            return new d(n)
                        }
                        return new c(n);
                    case r.AnnotationType.POPUP:
                        return new g(n);
                    case r.AnnotationType.LINE:
                        return new nt(n);
                    case r.AnnotationType.SQUARE:
                        return new tt(n);
                    case r.AnnotationType.CIRCLE:
                        return new it(n);
                    case r.AnnotationType.POLYLINE:
                        return new a(n);
                    case r.AnnotationType.POLYGON:
                        return new rt(n);
                    case r.AnnotationType.HIGHLIGHT:
                        return new ut(n);
                    case r.AnnotationType.UNDERLINE:
                        return new ft(n);
                    case r.AnnotationType.SQUIGGLY:
                        return new et(n);
                    case r.AnnotationType.STRIKEOUT:
                        return new ot(n);
                    case r.AnnotationType.STAMP:
                        return new st(n);
                    case r.AnnotationType.FILEATTACHMENT:
                        return new ht(n);
                    default:
                        return new s(n)
                    }
                }
            }]),
            n
        } (),
        s = function() {
            function n(t) {
                var i = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : !1,
                r = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : !1;
                u(this, n);
                this.isRenderable = i;
                this.data = t.data;
                this.layer = t.layer;
                this.page = t.page;
                this.viewport = t.viewport;
                this.linkService = t.linkService;
                this.downloadManager = t.downloadManager;
                this.imageResourcesPath = t.imageResourcesPath;
                this.renderInteractiveForms = t.renderInteractiveForms;
                this.svgFactory = t.svgFactory;
                i && (this.container = this._createContainer(r))
            }
            return f(n, [{
                key: "_createContainer",
                value: function() {
                    var l = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : !1,
                    n = this.data,
                    u = this.page,
                    a = this.viewport,
                    t = document.createElement("section"),
                    f = n.rect[2] - n.rect[0],
                    e = n.rect[3] - n.rect[1],
                    i,
                    o,
                    s,
                    c;
                    if (t.setAttribute("data-annotation-id", n.id), i = r.Util.normalizeRect([n.rect[0], u.view[3] - n.rect[1] + u.view[1], n.rect[2], u.view[3] - n.rect[3] + u.view[1]]), h.CustomStyle.setProp("transform", t, "matrix(" + a.transform.join(",") + ")"), h.CustomStyle.setProp("transformOrigin", t, -i[0] + "px " + -i[1] + "px"), !l && n.borderStyle.width > 0) {
                        t.style.borderWidth = n.borderStyle.width + "px";
                        n.borderStyle.style !== r.AnnotationBorderStyleType.UNDERLINE && (f = f - 2 * n.borderStyle.width, e = e - 2 * n.borderStyle.width);
                        o = n.borderStyle.horizontalCornerRadius;
                        s = n.borderStyle.verticalCornerRadius; (o > 0 || s > 0) && (c = o + "px / " + s + "px", h.CustomStyle.setProp("borderRadius", t, c));
                        switch (n.borderStyle.style) {
                        case r.AnnotationBorderStyleType.SOLID:
                            t.style.borderStyle = "solid";
                            break;
                        case r.AnnotationBorderStyleType.DASHED:
                            t.style.borderStyle = "dashed";
                            break;
                        case r.AnnotationBorderStyleType.BEVELED:
                            r.warn("Unimplemented border style: beveled");
                            break;
                        case r.AnnotationBorderStyleType.INSET:
                            r.warn("Unimplemented border style: inset");
                            break;
                        case r.AnnotationBorderStyleType.UNDERLINE:
                            t.style.borderBottomStyle = "solid"
                        }
                        n.color ? t.style.borderColor = r.Util.makeCssRgb(n.color[0] | 0, n.color[1] | 0, n.color[2] | 0) : t.style.borderWidth = 0
                    }
                    return t.style.left = i[0] + "px",
                    t.style.top = i[1] + "px",
                    t.style.width = f + "px",
                    t.style.height = e + "px",
                    t
                }
            },
            {
                key: "_createPopup",
                value: function(n, t, i) {
                    t || (t = document.createElement("div"), t.style.height = n.style.height, t.style.width = n.style.width, n.appendChild(t));
                    var u = new l({
                        container: n,
                        trigger: t,
                        color: i.color,
                        title: i.title,
                        contents: i.contents,
                        hideWrapper: !0
                    }),
                    r = u.render();
                    r.style.left = n.style.width;
                    n.appendChild(r)
                }
            },
            {
                key: "render",
                value: function() {
                    throw new Error("Abstract method `AnnotationElement.render` called");
                }
            }]),
            n
        } (),
        y = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.url || n.data.dest || n.data.action);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "linkAnnotation";
                    var n = document.createElement("a");
                    return h.addLinkAttributes(n, {
                        url: this.data.url,
                        target: this.data.newWindow ? h.LinkTarget.BLANK: undefined
                    }),
                    this.data.url || (this.data.action ? this._bindNamedAction(n, this.data.action) : this._bindLink(n, this.data.dest)),
                    this.container.appendChild(n),
                    this.container
                }
            },
            {
                key: "_bindLink",
                value: function(n, t) {
                    var i = this;
                    n.href = this.linkService.getDestinationHash(t);
                    n.onclick = function() {
                        return t && i.linkService.navigateTo(t),
                        !1
                    };
                    t && (n.className = "internalLink")
                }
            },
            {
                key: "_bindNamedAction",
                value: function(n, t) {
                    var i = this;
                    n.href = this.linkService.getAnchorUrl("");
                    n.onclick = function() {
                        return i.linkService.executeNamedAction(t),
                        !1
                    };
                    n.className = "internalLink"
                }
            }]),
            t
        } (s),
        p = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "textAnnotation";
                    var n = document.createElement("img");
                    return n.style.height = this.container.style.height,
                    n.style.width = this.container.style.width,
                    n.src = this.imageResourcesPath + "annotation-" + this.data.name.toLowerCase() + ".svg",
                    n.alt = "[{{type}} Annotation]",
                    n.dataset.l10nId = "text_annotation_type",
                    n.dataset.l10nArgs = JSON.stringify({
                        type: this.data.name
                    }),
                    this.data.hasPopup || this._createPopup(this.container, n, this.data),
                    this.container.appendChild(n),
                    this.container
                }
            }]),
            t
        } (s),
        c = function(n) {
            function t() {
                return u(this, t),
                e(this, (t.__proto__ || Object.getPrototypeOf(t)).apply(this, arguments))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container
                }
            }]),
            t
        } (s),
        w = function(n) {
            function t(n) {
                u(this, t);
                var i = n.renderInteractiveForms || !n.data.hasAppearance && !!n.data.fieldValue;
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    var n, i, r, t;
                    return this.container.className = "textWidgetAnnotation",
                    n = null,
                    this.renderInteractiveForms ? (this.data.multiLine ? (n = document.createElement("textarea"), n.textContent = this.data.fieldValue) : (n = document.createElement("input"), n.type = "text", n.setAttribute("value", this.data.fieldValue)), n.disabled = this.data.readOnly, this.data.maxLen !== null && (n.maxLength = this.data.maxLen), this.data.comb && (i = this.data.rect[2] - this.data.rect[0], r = i / this.data.maxLen, n.classList.add("comb"), n.style.letterSpacing = "calc(" + r + "px - 1ch)")) : (n = document.createElement("div"), n.textContent = this.data.fieldValue, n.style.verticalAlign = "middle", n.style.display = "table-cell", t = null, this.data.fontRefName && (t = this.page.commonObjs.getData(this.data.fontRefName)), this._setTextStyle(n, t)),
                    this.data.textAlignment !== null && (n.style.textAlign = ["left", "center", "right"][this.data.textAlignment]),
                    this.container.appendChild(n),
                    this.container
                }
            },
            {
                key: "_setTextStyle",
                value: function(n, t) {
                    var i = n.style,
                    r, u; (i.fontSize = this.data.fontSize + "px", i.direction = this.data.fontDirection < 0 ? "rtl": "ltr", t) && (i.fontWeight = t.black ? t.bold ? "900": "bold": t.bold ? "bold": "normal", i.fontStyle = t.italic ? "italic": "normal", r = t.loadedName ? '"' + t.loadedName + '", ': "", u = t.fallbackName || "Helvetica, sans-serif", i.fontFamily = r + u)
                }
            }]),
            t
        } (c),
        b = function(n) {
            function t(n) {
                return u(this, t),
                e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, n.renderInteractiveForms))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "buttonWidgetAnnotation checkBox";
                    var n = document.createElement("input");
                    return n.disabled = this.data.readOnly,
                    n.type = "checkbox",
                    this.data.fieldValue && this.data.fieldValue !== "Off" && n.setAttribute("checked", !0),
                    this.container.appendChild(n),
                    this.container
                }
            }]),
            t
        } (c),
        k = function(n) {
            function t(n) {
                return u(this, t),
                e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, n.renderInteractiveForms))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "buttonWidgetAnnotation radioButton";
                    var n = document.createElement("input");
                    return n.disabled = this.data.readOnly,
                    n.type = "radio",
                    n.name = this.data.fieldName,
                    this.data.fieldValue === this.data.buttonValue && n.setAttribute("checked", !0),
                    this.container.appendChild(n),
                    this.container
                }
            }]),
            t
        } (c),
        d = function(n) {
            function t(n) {
                return u(this, t),
                e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, n.renderInteractiveForms))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    var n, i, u, r, t;
                    for (this.container.className = "choiceWidgetAnnotation", n = document.createElement("select"), n.disabled = this.data.readOnly, this.data.combo || (n.size = this.data.options.length, this.data.multiSelect && (n.multiple = !0)), i = 0, u = this.data.options.length; i < u; i++) r = this.data.options[i],
                    t = document.createElement("option"),
                    t.textContent = r.displayValue,
                    t.value = r.exportValue,
                    this.data.fieldValue.indexOf(r.displayValue) >= 0 && t.setAttribute("selected", !0),
                    n.appendChild(t);
                    return this.container.appendChild(n),
                    this.container
                }
            }]),
            t
        } (c),
        g = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    var t, n;
                    if ((this.container.className = "popupAnnotation", ["Line", "Square", "Circle", "PolyLine", "Polygon"].indexOf(this.data.parentType) >= 0) || (t = '[data-annotation-id="' + this.data.parentId + '"]', n = this.layer.querySelector(t), !n)) return this.container;
                    var u = new l({
                        container: this.container,
                        trigger: n,
                        color: this.data.color,
                        title: this.data.title,
                        contents: this.data.contents
                    }),
                    i = parseFloat(n.style.left),
                    r = parseFloat(n.style.width);
                    return h.CustomStyle.setProp("transformOrigin", this.container, -(i + r) + "px -" + n.style.top),
                    this.container.style.left = i + r + "px",
                    this.container.appendChild(u.render()),
                    this.container
                }
            }]),
            t
        } (s),
        l = function() {
            function n(t) {
                u(this, n);
                this.container = t.container;
                this.trigger = t.trigger;
                this.color = t.color;
                this.title = t.title;
                this.contents = t.contents;
                this.hideWrapper = t.hideWrapper || !1;
                this.pinned = !1
            }
            return f(n, [{
                key: "render",
                value: function() {
                    var u = .7,
                    i = document.createElement("div"),
                    t,
                    n,
                    e,
                    f;
                    if (i.className = "popupWrapper", this.hideElement = this.hideWrapper ? i: this.container, this.hideElement.setAttribute("hidden", !0), t = document.createElement("div"), t.className = "popup", n = this.color, n) {
                        var o = u * (255 - n[0]) + n[0],
                        s = u * (255 - n[1]) + n[1],
                        h = u * (255 - n[2]) + n[2];
                        t.style.backgroundColor = r.Util.makeCssRgb(o | 0, s | 0, h | 0)
                    }
                    return e = this._formatContents(this.contents),
                    f = document.createElement("h1"),
                    f.textContent = this.title,
                    this.trigger.addEventListener("click", this._toggle.bind(this)),
                    this.trigger.addEventListener("mouseover", this._show.bind(this, !1)),
                    this.trigger.addEventListener("mouseout", this._hide.bind(this, !1)),
                    t.addEventListener("click", this._hide.bind(this, !0)),
                    t.appendChild(f),
                    t.appendChild(e),
                    i.appendChild(t),
                    i
                }
            },
            {
                key: "_formatContents",
                value: function(n) {
                    for (var f, i = document.createElement("p"), r = n.split(/(?:\r\n?|\n)/), t = 0, u = r.length; t < u; ++t) f = r[t],
                    i.appendChild(document.createTextNode(f)),
                    t < u - 1 && i.appendChild(document.createElement("br"));
                    return i
                }
            },
            {
                key: "_toggle",
                value: function() {
                    this.pinned ? this._hide(!0) : this._show(!0)
                }
            },
            {
                key: "_show",
                value: function() {
                    var n = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : !1;
                    n && (this.pinned = !0);
                    this.hideElement.hasAttribute("hidden") && (this.hideElement.removeAttribute("hidden"), this.container.style.zIndex += 1)
                }
            },
            {
                key: "_hide",
                value: function() {
                    var n = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : !0;
                    n && (this.pinned = !1);
                    this.hideElement.hasAttribute("hidden") || this.pinned || (this.hideElement.setAttribute("hidden", !0), this.container.style.zIndex -= 1)
                }
            }]),
            n
        } (),
        nt = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "lineAnnotation";
                    var n = this.data,
                    r = n.rect[2] - n.rect[0],
                    u = n.rect[3] - n.rect[1],
                    i = this.svgFactory.create(r, u),
                    t = this.svgFactory.createElement("svg:line");
                    return t.setAttribute("x1", n.rect[2] - n.lineCoordinates[0]),
                    t.setAttribute("y1", n.rect[3] - n.lineCoordinates[1]),
                    t.setAttribute("x2", n.rect[2] - n.lineCoordinates[2]),
                    t.setAttribute("y2", n.rect[3] - n.lineCoordinates[3]),
                    t.setAttribute("stroke-width", n.borderStyle.width),
                    t.setAttribute("stroke", "transparent"),
                    i.appendChild(t),
                    this.container.append(i),
                    this._createPopup(this.container, t, n),
                    this.container
                }
            }]),
            t
        } (s),
        tt = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "squareAnnotation";
                    var t = this.data,
                    r = t.rect[2] - t.rect[0],
                    u = t.rect[3] - t.rect[1],
                    f = this.svgFactory.create(r, u),
                    i = t.borderStyle.width,
                    n = this.svgFactory.createElement("svg:rect");
                    return n.setAttribute("x", i / 2),
                    n.setAttribute("y", i / 2),
                    n.setAttribute("width", r - i),
                    n.setAttribute("height", u - i),
                    n.setAttribute("stroke-width", i),
                    n.setAttribute("stroke", "transparent"),
                    n.setAttribute("fill", "none"),
                    f.appendChild(n),
                    this.container.append(f),
                    this._createPopup(this.container, n, t),
                    this.container
                }
            }]),
            t
        } (s),
        it = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "circleAnnotation";
                    var t = this.data,
                    i = t.rect[2] - t.rect[0],
                    r = t.rect[3] - t.rect[1],
                    f = this.svgFactory.create(i, r),
                    u = t.borderStyle.width,
                    n = this.svgFactory.createElement("svg:ellipse");
                    return n.setAttribute("cx", i / 2),
                    n.setAttribute("cy", r / 2),
                    n.setAttribute("rx", i / 2 - u / 2),
                    n.setAttribute("ry", r / 2 - u / 2),
                    n.setAttribute("stroke-width", u),
                    n.setAttribute("stroke", "transparent"),
                    n.setAttribute("fill", "none"),
                    f.appendChild(n),
                    this.container.append(f),
                    this._createPopup(this.container, n, t),
                    this.container
                }
            }]),
            t
        } (s),
        a = function(n) {
            function t(n) {
                u(this, t);
                var r = !!(n.data.hasPopup || n.data.title || n.data.contents),
                i = e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, r, !0));
                return i.containerClassName = "polylineAnnotation",
                i.svgElementName = "svg:polyline",
                i
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    var i, e, o, s, h, t;
                    this.container.className = this.containerClassName;
                    var n = this.data,
                    c = n.rect[2] - n.rect[0],
                    l = n.rect[3] - n.rect[1],
                    f = this.svgFactory.create(c, l),
                    u = n.vertices,
                    r = [];
                    for (i = 0, e = u.length; i < e; i++) o = u[i].x - n.rect[0],
                    s = n.rect[3] - u[i].y,
                    r.push(o + "," + s);
                    return r = r.join(" "),
                    h = n.borderStyle.width,
                    t = this.svgFactory.createElement(this.svgElementName),
                    t.setAttribute("points", r),
                    t.setAttribute("stroke-width", h),
                    t.setAttribute("stroke", "transparent"),
                    t.setAttribute("fill", "none"),
                    f.appendChild(t),
                    this.container.append(f),
                    this._createPopup(this.container, t, n),
                    this.container
                }
            }]),
            t
        } (s),
        rt = function(n) {
            function t(n) {
                u(this, t);
                var i = e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n));
                return i.containerClassName = "polygonAnnotation",
                i.svgElementName = "svg:polygon",
                i
            }
            return o(t, n),
            t
        } (a),
        ut = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container.className = "highlightAnnotation",
                    this.data.hasPopup || this._createPopup(this.container, null, this.data),
                    this.container
                }
            }]),
            t
        } (s),
        ft = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container.className = "underlineAnnotation",
                    this.data.hasPopup || this._createPopup(this.container, null, this.data),
                    this.container
                }
            }]),
            t
        } (s),
        et = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container.className = "squigglyAnnotation",
                    this.data.hasPopup || this._createPopup(this.container, null, this.data),
                    this.container
                }
            }]),
            t
        } (s),
        ot = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container.className = "strikeoutAnnotation",
                    this.data.hasPopup || this._createPopup(this.container, null, this.data),
                    this.container
                }
            }]),
            t
        } (s),
        st = function(n) {
            function t(n) {
                u(this, t);
                var i = !!(n.data.hasPopup || n.data.title || n.data.contents);
                return e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, i, !0))
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    return this.container.className = "stampAnnotation",
                    this.data.hasPopup || this._createPopup(this.container, null, this.data),
                    this.container
                }
            }]),
            t
        } (s),
        ht = function(n) {
            function t(n) {
                u(this, t);
                var i = e(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n, !0)),
                f = i.data.file;
                i.filename = h.getFilenameFromUrl(f.filename);
                i.content = f.content;
                i.linkService.onFileAttachmentAnnotation({
                    id: r.stringToPDFString(f.filename),
                    filename: f.filename,
                    content: f.content
                });
                return i
            }
            return o(t, n),
            f(t, [{
                key: "render",
                value: function() {
                    this.container.className = "fileAttachmentAnnotation";
                    var n = document.createElement("div");
                    return n.style.height = this.container.style.height,
                    n.style.width = this.container.style.width,
                    n.addEventListener("dblclick", this._download.bind(this)),
                    !this.data.hasPopup && (this.data.title || this.data.contents) && this._createPopup(this.container, n, this.data),
                    this.container.appendChild(n),
                    this.container
                }
            },
            {
                key: "_download",
                value: function() {
                    if (!this.downloadManager) {
                        r.warn("Download cannot be started due to unavailable download manager");
                        return
                    }
                    this.downloadManager.downloadData(this.content, this.filename, "")
                }
            }]),
            t
        } (s),
        ct = function() {
            function n() {
                u(this, n)
            }
            return f(n, null, [{
                key: "render",
                value: function(n) {
                    for (var i, r, t = 0,
                    u = n.annotations.length; t < u; t++)(i = n.annotations[t], i) && (r = v.create({
                        data: i,
                        layer: n.div,
                        page: n.page,
                        viewport: n.viewport,
                        linkService: n.linkService,
                        downloadManager: n.downloadManager,
                        imageResourcesPath: n.imageResourcesPath || h.getDefaultSetting("imageResourcesPath"),
                        renderInteractiveForms: n.renderInteractiveForms || !1,
                        svgFactory: new h.DOMSVGFactory
                    }), r.isRenderable && n.div.appendChild(r.render()))
                }
            },
            {
                key: "update",
                value: function(n) {
                    for (var u, i, t = 0,
                    r = n.annotations.length; t < r; t++) u = n.annotations[t],
                    i = n.div.querySelector('[data-annotation-id="' + u.id + '"]'),
                    i && h.CustomStyle.setProp("transform", i, "matrix(" + n.viewport.transform.join(",") + ")");
                    n.div.removeAttribute("hidden")
                }
            }]),
            n
        } ();
        t.AnnotationLayer = ct
    },
    function(n, t, i) {
        "use strict";
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.renderTextLayer = undefined;
        var r = i(0),
        u = i(8),
        f = function() {
            function s(n) {
                return ! o.test(n)
            }
            function h(t, i, f) {
                var h = document.createElement("div"),
                a = {
                    style: null,
                    angle: 0,
                    canvasWidth: 0,
                    isWhitespace: !1,
                    originalTransform: null,
                    paddingBottom: 0,
                    paddingLeft: 0,
                    paddingRight: 0,
                    paddingTop: 0,
                    scale: 1
                },
                w,
                l,
                v,
                y,
                b,
                k,
                d,
                g,
                nt,
                p;
                if (t._textDivs.push(h), s(i.str)) {
                    a.isWhitespace = !0;
                    t._textDivProperties.set(h, a);
                    return
                }
                var e = r.Util.transform(t._viewport.transform, i.transform),
                o = Math.atan2(e[1], e[0]),
                c = f[i.fontName];
                c.vertical && (o += Math.PI / 2);
                w = Math.sqrt(e[2] * e[2] + e[3] * e[3]);
                l = w;
                c.ascent ? l = c.ascent * l: c.descent && (l = (1 + c.descent) * l);
                o === 0 ? (v = e[4], y = e[5] - l) : (v = e[4] + l * Math.sin(o), y = e[5] - l * Math.cos(o));
                n[1] = v;
                n[3] = y;
                n[5] = w;
                n[7] = c.fontFamily;
                a.style = n.join("");
                h.setAttribute("style", a.style);
                h.textContent = i.str;
                u.getDefaultSetting("pdfBug") && (h.dataset.fontName = i.fontName);
                o !== 0 && (a.angle = o * (180 / Math.PI));
                i.str.length > 1 && (a.canvasWidth = c.vertical ? i.height * t._viewport.scale: i.width * t._viewport.scale);
                t._textDivProperties.set(h, a);
                t._textContentStream && t._layoutText(h);
                t._enhanceTextSelection && (b = 1, k = 0, o !== 0 && (b = Math.cos(o), k = Math.sin(o)), d = (c.vertical ? i.height: i.width) * t._viewport.scale, g = w, o !== 0 ? (nt = [b, k, -k, b, v, y], p = r.Util.getAxialAlignedBoundingBox([0, 0, d, g], nt)) : p = [v, y, v + d, y + g], t._bounds.push({
                    left: p[0],
                    top: p[1],
                    right: p[2],
                    bottom: p[3],
                    div: h,
                    size: [d, g],
                    m: nt
                }))
            }
            function t(n) {
                var t;
                if (!n._canceled) {
                    var i = n._textDivs,
                    r = n._capability,
                    u = i.length;
                    if (u > e) {
                        n._renderingDone = !0;
                        r.resolve();
                        return
                    }
                    if (!n._textContentStream) for (t = 0; t < u; t++) n._layoutText(i[t]);
                    n._renderingDone = !0;
                    r.resolve()
                }
            }
            function c(n) {
                for (var v, o, c, a, s = n._bounds,
                w = n._viewport,
                h = l(w.width, w.height, s), e = 0; e < h.length; e++) {
                    if (v = s[e].div, o = n._textDivProperties.get(v), o.angle === 0) {
                        o.paddingLeft = s[e].left - h[e].left;
                        o.paddingTop = s[e].top - h[e].top;
                        o.paddingRight = h[e].right - s[e].right;
                        o.paddingBottom = h[e].bottom - s[e].bottom;
                        n._textDivProperties.set(v, o);
                        continue
                    }
                    var f = h[e],
                    y = s[e],
                    p = y.m,
                    i = p[0],
                    u = p[1],
                    b = [[0, 0], [0, y.size[1]], [y.size[0], 0], y.size],
                    t = new Float64Array(64);
                    b.forEach(function(n, e) {
                        var o = r.Util.applyTransform(n, p);
                        t[e + 0] = i && (f.left - o[0]) / i;
                        t[e + 4] = u && (f.top - o[1]) / u;
                        t[e + 8] = i && (f.right - o[0]) / i;
                        t[e + 12] = u && (f.bottom - o[1]) / u;
                        t[e + 16] = u && (f.left - o[0]) / -u;
                        t[e + 20] = i && (f.top - o[1]) / i;
                        t[e + 24] = u && (f.right - o[0]) / -u;
                        t[e + 28] = i && (f.bottom - o[1]) / i;
                        t[e + 32] = i && (f.left - o[0]) / -i;
                        t[e + 36] = u && (f.top - o[1]) / -u;
                        t[e + 40] = i && (f.right - o[0]) / -i;
                        t[e + 44] = u && (f.bottom - o[1]) / -u;
                        t[e + 48] = u && (f.left - o[0]) / u;
                        t[e + 52] = i && (f.top - o[1]) / -i;
                        t[e + 56] = u && (f.right - o[0]) / u;
                        t[e + 60] = i && (f.bottom - o[1]) / -i
                    });
                    c = function(n, t, i) {
                        for (var u, r = 0,
                        f = 0; f < i; f++) u = n[t++],
                        u > 0 && (r = r ? Math.min(u, r) : u);
                        return r
                    };
                    a = 1 + Math.min(Math.abs(i), Math.abs(u));
                    o.paddingLeft = c(t, 32, 16) / a;
                    o.paddingTop = c(t, 48, 16) / a;
                    o.paddingRight = c(t, 0, 16) / a;
                    o.paddingBottom = c(t, 16, 16) / a;
                    n._textDivProperties.set(v, o)
                }
            }
            function l(n, t, r) {
                var f = r.map(function(n, t) {
                    return {
                        x1: n.left,
                        y1: n.top,
                        x2: n.right,
                        y2: n.bottom,
                        index: t,
                        x1New: undefined,
                        x2New: undefined
                    }
                }),
                u;
                return i(n, f),
                u = new Array(r.length),
                f.forEach(function(n) {
                    var t = n.index;
                    u[t] = {
                        left: n.x1New,
                        top: 0,
                        right: n.x2New,
                        bottom: 0
                    }
                }),
                r.map(function(t, i) {
                    var e = u[i],
                    r = f[i];
                    r.x1 = t.top;
                    r.y1 = n - e.right;
                    r.x2 = t.bottom;
                    r.y2 = n - e.left;
                    r.index = i;
                    r.x1New = undefined;
                    r.x2New = undefined
                }),
                i(t, f),
                f.forEach(function(n) {
                    var t = n.index;
                    u[t].top = n.x1New;
                    u[t].bottom = n.x2New
                }),
                u
            }
            function i(n, t) {
                t.sort(function(n, t) {
                    return n.x1 - t.x1 || n.index - t.index
                });
                var i = [{
                    start: -Infinity,
                    end: Infinity,
                    boundary: {
                        x1: -Infinity,
                        y1: -Infinity,
                        x2: 0,
                        y2: Infinity,
                        index: -1,
                        x1New: 0,
                        x2New: 0
                    }
                }];
                t.forEach(function(n) {
                    for (var f = 0,
                    e, s, t, r, u, h, a, o, v, l, c; f < i.length && i[f].end <= n.y1;) f++;
                    for (e = i.length - 1; e >= 0 && i[e].start >= n.y2;) e--;
                    for (h = -Infinity, r = f; r <= e; r++) s = i[r],
                    t = s.boundary,
                    a = t.x2 > n.x1 ? t.index > n.index ? t.x1New: n.x1: t.x2New === undefined ? (t.x2 + n.x1) / 2 : t.x2New,
                    a > h && (h = a);
                    for (n.x1New = h, r = f; r <= e; r++) s = i[r],
                    t = s.boundary,
                    t.x2New === undefined ? t.x2 > n.x1 ? t.index > n.index && (t.x2New = t.x2) : t.x2New = h: t.x2New > h && (t.x2New = Math.max(h, t.x2));
                    for (o = [], v = null, r = f; r <= e; r++) s = i[r],
                    t = s.boundary,
                    l = t.x2 > n.x2 ? t: n,
                    v === l ? o[o.length - 1].end = s.end: (o.push({
                        start: s.start,
                        end: s.end,
                        boundary: l
                    }), v = l);
                    for (i[f].start < n.y1 && (o[0].start = n.y1, o.unshift({
                        start: i[f].start,
                        end: n.y1,
                        boundary: i[f].boundary
                    })), n.y2 < i[e].end && (o[o.length - 1].end = n.y2, o.push({
                        start: n.y2,
                        end: i[e].end,
                        boundary: i[e].boundary
                    })), r = f; r <= e; r++) if (s = i[r], t = s.boundary, t.x2New === undefined) {
                        for (c = !1, u = f - 1; ! c && u >= 0 && i[u].start >= t.y1; u--) c = i[u].boundary === t;
                        for (u = e + 1; ! c && u < i.length && i[u].end <= t.y2; u++) c = i[u].boundary === t;
                        for (u = 0; ! c && u < o.length; u++) c = o[u].boundary === t;
                        c || (t.x2New = h)
                    }
                    Array.prototype.splice.apply(i, [f, e - f + 1].concat(o))
                });
                i.forEach(function(t) {
                    var i = t.boundary;
                    i.x2New === undefined && (i.x2New = Math.max(n, i.x2))
                })
            }
            function f(n) {
                var t = n.textContent,
                i = n.textContentStream,
                u = n.container,
                f = n.viewport,
                e = n.textDivs,
                o = n.textContentItemsStr,
                s = n.enhanceTextSelection;
                this._textContent = t;
                this._textContentStream = i;
                this._container = u;
                this._viewport = f;
                this._textDivs = e || [];
                this._textContentItemsStr = o || [];
                this._enhanceTextSelection = !!s;
                this._reader = null;
                this._layoutTextLastFontSize = null;
                this._layoutTextLastFontFamily = null;
                this._layoutTextCtx = null;
                this._textDivProperties = new WeakMap;
                this._renderingDone = !1;
                this._canceled = !1;
                this._capability = r.createPromiseCapability();
                this._renderTimer = null;
                this._bounds = []
            }
            function a(n) {
                var t = new f({
                    textContent: n.textContent,
                    textContentStream: n.textContentStream,
                    container: n.container,
                    viewport: n.viewport,
                    textDivs: n.textDivs,
                    textContentItemsStr: n.textContentItemsStr,
                    enhanceTextSelection: n.enhanceTextSelection
                });
                return t._render(n.timeout),
                t
            }
            var e = 1e5,
            o = /\S/,
            n = ["left: ", 0, "px; top: ", 0, "px; font-size: ", 0, "px; font-family: ", "", ";"];
            return f.prototype = {
                get promise() {
                    return this._capability.promise
                },
                cancel: function() {
                    this._reader && (this._reader.cancel(new r.AbortException("text layer task cancelled")), this._reader = null);
                    this._canceled = !0;
                    this._renderTimer !== null && (clearTimeout(this._renderTimer), this._renderTimer = null);
                    this._capability.reject("canceled")
                },
                _processItems: function(n, t) {
                    for (var i = 0,
                    r = n.length; i < r; i++) this._textContentItemsStr.push(n[i].str),
                    h(this, n[i], t)
                },
                _layoutText: function(n) {
                    var o = this._container,
                    t = this._textDivProperties.get(n),
                    r,
                    f,
                    e,
                    i;
                    t.isWhitespace || (r = n.style.fontSize, f = n.style.fontFamily, (r !== this._layoutTextLastFontSize || f !== this._layoutTextLastFontFamily) && (this._layoutTextCtx.font = r + " " + f, this._lastFontSize = r, this._lastFontFamily = f), e = this._layoutTextCtx.measureText(n.textContent).width, i = "", t.canvasWidth !== 0 && e > 0 && (t.scale = t.canvasWidth / e, i = "scaleX(" + t.scale + ")"), t.angle !== 0 && (i = "rotate(" + t.angle + "deg) " + i), i !== "" && (t.originalTransform = i, u.CustomStyle.setProp("transform", n, i)), this._textDivProperties.set(n, t), o.appendChild(n))
                },
                _render: function(n) {
                    var i = this,
                    u = r.createPromiseCapability(),
                    f = Object.create(null),
                    o = document.createElement("canvas"),
                    s,
                    h,
                    e;
                    if (o.mozOpaque = !0, this._layoutTextCtx = o.getContext("2d", {
                        alpha: !1
                    }), this._textContent) s = this._textContent.items,
                    h = this._textContent.styles,
                    this._processItems(s, h),
                    u.resolve();
                    else if (this._textContentStream) e = function e() {
                        i._reader.read().then(function(n) {
                            var t = n.value,
                            o = n.done;
                            if (o) {
                                u.resolve();
                                return
                            }
                            r.Util.extendObj(f, t.styles);
                            i._processItems(t.items, f);
                            e()
                        },
                        u.reject)
                    },
                    this._reader = this._textContentStream.getReader(),
                    e();
                    else throw new Error('Neither "textContent" nor "textContentStream" parameters specified.');
                    u.promise.then(function() {
                        f = null;
                        n ? i._renderTimer = setTimeout(function() {
                            t(i);
                            i._renderTimer = null
                        },
                        n) : t(i)
                    },
                    this._capability.reject)
                },
                expandTextDivs: function(n) {
                    var e, o, f, t, i, r;
                    if (this._enhanceTextSelection && this._renderingDone) for (this._bounds !== null && (c(this), this._bounds = null), e = 0, o = this._textDivs.length; e < o; e++)(f = this._textDivs[e], t = this._textDivProperties.get(f), t.isWhitespace) || (n ? (i = "", r = "", t.scale !== 1 && (i = "scaleX(" + t.scale + ")"), t.angle !== 0 && (i = "rotate(" + t.angle + "deg) " + i), t.paddingLeft !== 0 && (r += " padding-left: " + t.paddingLeft / t.scale + "px;", i += " translateX(" + -t.paddingLeft / t.scale + "px)"), t.paddingTop !== 0 && (r += " padding-top: " + t.paddingTop + "px;", i += " translateY(" + -t.paddingTop + "px)"), t.paddingRight !== 0 && (r += " padding-right: " + t.paddingRight / t.scale + "px;"), t.paddingBottom !== 0 && (r += " padding-bottom: " + t.paddingBottom + "px;"), r !== "" && f.setAttribute("style", t.style + r), i !== "" && u.CustomStyle.setProp("transform", f, i)) : (f.style.padding = 0, u.CustomStyle.setProp("transform", f, t.originalTransform || "")))
                }
            },
            a
        } ();
        t.renderTextLayer = f
    },
    function(n, t, i) {
        "use strict";
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.SVGGraphics = undefined;
        var r = i(0),
        e = i(8),
        f = function() {
            throw new Error("Not implemented: SVGGraphics");
        },
        u = {
            fontStyle: "normal",
            fontWeight: "normal",
            fillColor: "#000000"
        },
        o = function() {
            function h(n, t, i) {
                for (var f, e, r = -1,
                u = t; u < i; u++) f = (r ^ n[u]) & 255,
                e = o[f],
                r = r >>> 8 ^ e;
                return r ^ -1
            }
            function e(n, t, i, r) {
                var u = r,
                e = t.length,
                f;
                i[u] = e >> 24 & 255;
                i[u + 1] = e >> 16 & 255;
                i[u + 2] = e >> 8 & 255;
                i[u + 3] = e & 255;
                u += 4;
                i[u] = n.charCodeAt(0) & 255;
                i[u + 1] = n.charCodeAt(1) & 255;
                i[u + 2] = n.charCodeAt(2) & 255;
                i[u + 3] = n.charCodeAt(3) & 255;
                u += 4;
                i.set(t, u);
                u += t.length;
                f = h(i, r + 4, u);
                i[u] = f >> 24 & 255;
                i[u + 1] = f >> 16 & 255;
                i[u + 2] = f >> 8 & 255;
                i[u + 3] = f & 255
            }
            function c(n, t, i) {
                for (var r = 1,
                u = 0,
                f = t; f < i; ++f) r = (r + (n[f] & 255)) % 65521,
                u = (u + r) % 65521;
                return u << 16 | r
            }
            function l(n) {
                var i, t;
                if (!r.isNodeJS()) return s(n);
                try {
                    return i = parseInt(process.versions.node) >= 8 ? n: new Buffer(n),
                    t = require("zlib").deflateSync(i, {
                        level: 9
                    }),
                    t instanceof Uint8Array ? t: new Uint8Array(t)
                } catch(u) {
                    r.warn("Not compressing PNG because zlib.deflateSync is unavailable: " + u)
                }
                return s(n)
            }
            function s(n) {
                var r = n.length,
                u = 65535,
                o = Math.ceil(r / u),
                i = new Uint8Array(2 + r + o * 5 + 4),
                t = 0,
                f,
                e;
                for (i[t++] = 120, i[t++] = 156, f = 0; r > u;) i[t++] = 0,
                i[t++] = 255,
                i[t++] = 255,
                i[t++] = 0,
                i[t++] = 0,
                i.set(n.subarray(f, f + u), t),
                t += u,
                f += u,
                r -= u;
                return i[t++] = 1,
                i[t++] = r & 255,
                i[t++] = r >> 8 & 255,
                i[t++] = ~r & 255,
                i[t++] = (~r & 65535) >> 8 & 255,
                i.set(n.subarray(f), t),
                t += n.length - f,
                e = c(n, 0, n.length),
                i[t++] = e >> 24 & 255,
                i[t++] = e >> 16 & 255,
                i[t++] = e >> 8 & 255,
                i[t++] = e & 255,
                i
            }
            function a(n, t, f) {
                var s = n.width,
                h = n.height,
                p, w, o, tt = n.data;
                switch (t) {
                case r.ImageKind.GRAYSCALE_1BPP:
                    w = 0;
                    p = 1;
                    o = s + 7 >> 3;
                    break;
                case r.ImageKind.RGB_24BPP:
                    w = 2;
                    p = 8;
                    o = s * 3;
                    break;
                case r.ImageKind.RGBA_32BPP:
                    w = 6;
                    p = 8;
                    o = s * 4;
                    break;
                default:
                    throw new Error("invalid format");
                }
                for (var b = new Uint8Array((1 + o) * h), a = 0, k = 0, d, v = 0; v < h; ++v) b[a++] = 0,
                b.set(tt.subarray(k, k + o), a),
                k += o,
                a += o;
                if (t === r.ImageKind.GRAYSCALE_1BPP) for (a = 0, v = 0; v < h; v++) for (a++, d = 0; d < o; d++) b[a++] ^= 255;
                var g = new Uint8Array([s >> 24 & 255, s >> 16 & 255, s >> 8 & 255, s & 255, h >> 24 & 255, h >> 16 & 255, h >> 8 & 255, h & 255, p, w, 0, 0, 0]),
                nt = l(b),
                it = i.length + u * 3 + g.length + nt.length,
                y = new Uint8Array(it),
                c = 0;
                return y.set(i, c),
                c += i.length,
                e("IHDR", g, y, c),
                c += u + g.length,
                e("IDATA", nt, y, c),
                c += u + nt.length,
                e("IEND", new Uint8Array(0), y, c),
                r.createObjectURL(y, "image/png", f)
            }
            for (var n, f, i = new Uint8Array([137, 80, 78, 71, 13, 10, 26, 10]), u = 12, o = new Int32Array(256), t = 0; t < 256; t++) {
                for (n = t, f = 0; f < 8; f++) n = n & 1 ? 3988292384 ^ n >> 1 & 2147483647 : n >> 1 & 2147483647;
                o[t] = n
            }
            return function(n, t) {
                var i = n.kind === undefined ? r.ImageKind.GRAYSCALE_1BPP: n.kind;
                return a(n, i, t)
            }
        } (),
        s = function() {
            function n() {
                this.fontSizeScale = 1;
                this.fontWeight = u.fontWeight;
                this.fontSize = 0;
                this.textMatrix = r.IDENTITY_MATRIX;
                this.fontMatrix = r.FONT_IDENTITY_MATRIX;
                this.leading = 0;
                this.x = 0;
                this.y = 0;
                this.lineX = 0;
                this.lineY = 0;
                this.charSpacing = 0;
                this.wordSpacing = 0;
                this.textHScale = 1;
                this.textRise = 0;
                this.fillColor = u.fillColor;
                this.strokeColor = "#000000";
                this.fillAlpha = 1;
                this.strokeAlpha = 1;
                this.lineWidth = 1;
                this.lineJoin = "";
                this.lineCap = "";
                this.miterLimit = 0;
                this.dashArray = [];
                this.dashPhase = 0;
                this.dependencies = [];
                this.activeClipUrl = null;
                this.clipGroup = null;
                this.maskId = ""
            }
            return n.prototype = {
                clone: function() {
                    return Object.create(this)
                },
                setCurrentPoint: function(n, t) {
                    this.x = n;
                    this.y = t
                }
            },
            n
        } ();
        t.SVGGraphics = f = function() {
            function c(n) {
                for (var t = [], r = [], u = n.length, i = 0; i < u; i++) {
                    if (n[i].fn === "save") {
                        t.push({
                            fnId: 92,
                            fn: "group",
                            items: []
                        });
                        r.push(t);
                        t = t[t.length - 1].items;
                        continue
                    }
                    n[i].fn === "restore" ? t = r.pop() : t.push(n[i])
                }
                return t
            }
            function n(n) {
                if (Number.isInteger(n)) return n.toString();
                var t = n.toFixed(10),
                i = t.length - 1;
                if (t[i] !== "0") return t;
                do i--;
                while (t[i] === "0");
                return t.substr(0, t[i] === "." ? i: i + 1)
            }
            function t(t) {
                if (t[4] === 0 && t[5] === 0) {
                    if (t[1] === 0 && t[2] === 0) return t[0] === 1 && t[3] === 1 ? "": "scale(" + n(t[0]) + " " + n(t[3]) + ")";
                    if (t[0] === t[3] && t[1] === -t[2]) {
                        var i = Math.acos(t[0]) * 180 / Math.PI;
                        return "rotate(" + n(i) + ")"
                    }
                } else if (t[0] === 1 && t[1] === 0 && t[2] === 0 && t[3] === 1) return "translate(" + n(t[4]) + " " + n(t[5]) + ")";
                return "matrix(" + n(t[0]) + " " + n(t[1]) + " " + n(t[2]) + " " + n(t[3]) + " " + n(t[4]) + " " + n(t[5]) + ")"
            }
            function i(n, t, i) {
                this.svgFactory = new e.DOMSVGFactory;
                this.current = new s;
                this.transformMatrix = r.IDENTITY_MATRIX;
                this.transformStack = [];
                this.extraStack = [];
                this.commonObjs = n;
                this.objs = t;
                this.pendingClip = null;
                this.pendingEOFill = !1;
                this.embedFonts = !1;
                this.embeddedFonts = Object.create(null);
                this.cssStyle = null;
                this.forceDataSchema = !!i
            }
            var l = "http://www.w3.org/XML/1998/namespace",
            f = "http://www.w3.org/1999/xlink",
            a = ["butt", "round", "square"],
            v = ["miter", "round", "bevel"],
            h = 0,
            y = 0;
            return i.prototype = {
                save: function() {
                    this.transformStack.push(this.transformMatrix);
                    var n = this.current;
                    this.extraStack.push(n);
                    this.current = n.clone()
                },
                restore: function() {
                    this.transformMatrix = this.transformStack.pop();
                    this.current = this.extraStack.pop();
                    this.pendingClip = null;
                    this.tgrp = null
                },
                group: function(n) {
                    this.save();
                    this.executeOpTree(n);
                    this.restore()
                },
                loadDependencies: function(n) {
                    for (var f, i, s, u, h, c, e = this,
                    o = n.fnArray,
                    l = o.length,
                    a = n.argsArray,
                    t = 0; t < l; t++) if (r.OPS.dependency === o[t]) for (f = a[t], i = 0, s = f.length; i < s; i++) u = f[i],
                    h = u.substring(0, 2) === "g_",
                    c = h ? new Promise(function(n) {
                        e.commonObjs.get(u, n)
                    }) : new Promise(function(n) {
                        e.objs.get(u, n)
                    }),
                    this.current.dependencies.push(c);
                    return Promise.all(this.current.dependencies)
                },
                transform: function(n, t, i, u, f, e) {
                    var o = [n, t, i, u, f, e];
                    this.transformMatrix = r.Util.transform(this.transformMatrix, o);
                    this.tgrp = null
                },
                getSVG: function(n, t) {
                    var i = this,
                    u;
                    return this.viewport = t,
                    u = this._initialize(t),
                    this.loadDependencies(n).then(function() {
                        i.transformMatrix = r.IDENTITY_MATRIX;
                        var t = i.convertOpList(n);
                        return i.executeOpTree(t),
                        u
                    })
                },
                convertOpList: function(n) {
                    var s = n.argsArray,
                    f = n.fnArray,
                    h = f.length,
                    e = [],
                    o = [],
                    i,
                    t,
                    u;
                    for (i in r.OPS) e[r.OPS[i]] = i;
                    for (t = 0; t < h; t++) u = f[t],
                    o.push({
                        fnId: u,
                        fn: e[u],
                        args: s[t]
                    });
                    return c(o)
                },
                executeOpTree: function(n) {
                    for (var u = n.length,
                    i = 0; i < u; i++) {
                        var f = n[i].fn,
                        e = n[i].fnId,
                        t = n[i].args;
                        switch (e | 0) {
                        case r.OPS.beginText:
                            this.beginText();
                            break;
                        case r.OPS.setLeading:
                            this.setLeading(t);
                            break;
                        case r.OPS.setLeadingMoveText:
                            this.setLeadingMoveText(t[0], t[1]);
                            break;
                        case r.OPS.setFont:
                            this.setFont(t);
                            break;
                        case r.OPS.showText:
                            this.showText(t[0]);
                            break;
                        case r.OPS.showSpacedText:
                            this.showText(t[0]);
                            break;
                        case r.OPS.endText:
                            this.endText();
                            break;
                        case r.OPS.moveText:
                            this.moveText(t[0], t[1]);
                            break;
                        case r.OPS.setCharSpacing:
                            this.setCharSpacing(t[0]);
                            break;
                        case r.OPS.setWordSpacing:
                            this.setWordSpacing(t[0]);
                            break;
                        case r.OPS.setHScale:
                            this.setHScale(t[0]);
                            break;
                        case r.OPS.setTextMatrix:
                            this.setTextMatrix(t[0], t[1], t[2], t[3], t[4], t[5]);
                            break;
                        case r.OPS.setTextRise:
                            this.setTextRise(t[0]);
                            break;
                        case r.OPS.setLineWidth:
                            this.setLineWidth(t[0]);
                            break;
                        case r.OPS.setLineJoin:
                            this.setLineJoin(t[0]);
                            break;
                        case r.OPS.setLineCap:
                            this.setLineCap(t[0]);
                            break;
                        case r.OPS.setMiterLimit:
                            this.setMiterLimit(t[0]);
                            break;
                        case r.OPS.setFillRGBColor:
                            this.setFillRGBColor(t[0], t[1], t[2]);
                            break;
                        case r.OPS.setStrokeRGBColor:
                            this.setStrokeRGBColor(t[0], t[1], t[2]);
                            break;
                        case r.OPS.setDash:
                            this.setDash(t[0], t[1]);
                            break;
                        case r.OPS.setGState:
                            this.setGState(t[0]);
                            break;
                        case r.OPS.fill:
                            this.fill();
                            break;
                        case r.OPS.eoFill:
                            this.eoFill();
                            break;
                        case r.OPS.stroke:
                            this.stroke();
                            break;
                        case r.OPS.fillStroke:
                            this.fillStroke();
                            break;
                        case r.OPS.eoFillStroke:
                            this.eoFillStroke();
                            break;
                        case r.OPS.clip:
                            this.clip("nonzero");
                            break;
                        case r.OPS.eoClip:
                            this.clip("evenodd");
                            break;
                        case r.OPS.paintSolidColorImageMask:
                            this.paintSolidColorImageMask();
                            break;
                        case r.OPS.paintJpegXObject:
                            this.paintJpegXObject(t[0], t[1], t[2]);
                            break;
                        case r.OPS.paintImageXObject:
                            this.paintImageXObject(t[0]);
                            break;
                        case r.OPS.paintInlineImageXObject:
                            this.paintInlineImageXObject(t[0]);
                            break;
                        case r.OPS.paintImageMaskXObject:
                            this.paintImageMaskXObject(t[0]);
                            break;
                        case r.OPS.paintFormXObjectBegin:
                            this.paintFormXObjectBegin(t[0], t[1]);
                            break;
                        case r.OPS.paintFormXObjectEnd:
                            this.paintFormXObjectEnd();
                            break;
                        case r.OPS.closePath:
                            this.closePath();
                            break;
                        case r.OPS.closeStroke:
                            this.closeStroke();
                            break;
                        case r.OPS.closeFillStroke:
                            this.closeFillStroke();
                            break;
                        case r.OPS.nextLine:
                            this.nextLine();
                            break;
                        case r.OPS.transform:
                            this.transform(t[0], t[1], t[2], t[3], t[4], t[5]);
                            break;
                        case r.OPS.constructPath:
                            this.constructPath(t[0], t[1]);
                            break;
                        case r.OPS.endPath:
                            this.endPath();
                            break;
                        case 92:
                            this.group(n[i].items);
                            break;
                        default:
                            r.warn("Unimplemented operator " + f)
                        }
                    }
                },
                setWordSpacing: function(n) {
                    this.current.wordSpacing = n
                },
                setCharSpacing: function(n) {
                    this.current.charSpacing = n
                },
                nextLine: function() {
                    this.moveText(0, this.current.leading)
                },
                setTextMatrix: function(t, i, r, u, f, e) {
                    var o = this.current;
                    this.current.textMatrix = this.current.lineMatrix = [t, i, r, u, f, e];
                    this.current.x = this.current.lineX = 0;
                    this.current.y = this.current.lineY = 0;
                    o.xcoords = [];
                    o.tspan = this.svgFactory.createElement("svg:tspan");
                    o.tspan.setAttributeNS(null, "font-family", o.fontFamily);
                    o.tspan.setAttributeNS(null, "font-size", n(o.fontSize) + "px");
                    o.tspan.setAttributeNS(null, "y", n( - o.y));
                    o.txtElement = this.svgFactory.createElement("svg:text");
                    o.txtElement.appendChild(o.tspan)
                },
                beginText: function() {
                    this.current.x = this.current.lineX = 0;
                    this.current.y = this.current.lineY = 0;
                    this.current.textMatrix = r.IDENTITY_MATRIX;
                    this.current.lineMatrix = r.IDENTITY_MATRIX;
                    this.current.tspan = this.svgFactory.createElement("svg:tspan");
                    this.current.txtElement = this.svgFactory.createElement("svg:text");
                    this.current.txtgrp = this.svgFactory.createElement("svg:g");
                    this.current.xcoords = []
                },
                moveText: function(t, i) {
                    var r = this.current;
                    this.current.x = this.current.lineX += t;
                    this.current.y = this.current.lineY += i;
                    r.xcoords = [];
                    r.tspan = this.svgFactory.createElement("svg:tspan");
                    r.tspan.setAttributeNS(null, "font-family", r.fontFamily);
                    r.tspan.setAttributeNS(null, "font-size", n(r.fontSize) + "px");
                    r.tspan.setAttributeNS(null, "y", n( - r.y))
                },
                showText: function(i) {
                    var f = this.current,
                    y = f.font,
                    c = f.fontSize,
                    e, s;
                    if (c !== 0) {
                        for (var b = f.charSpacing,
                        p = f.wordSpacing,
                        a = f.fontDirection,
                        v = f.textHScale * a,
                        k = i.length,
                        d = y.vertical,
                        g = c * f.fontMatrix[0], o = 0, h = 0; h < k; ++h) {
                            if (e = i[h], e === null) {
                                o += a * p;
                                continue
                            } else if (r.isNum(e)) {
                                o += -e * c * .001;
                                continue
                            }
                            var nt = e.width,
                            tt = e.fontChar,
                            it = (e.isSpace ? p: 0) + b,
                            w = nt * g + it * a;
                            if (!e.isInFont && !y.missingFile) {
                                o += w;
                                continue
                            }
                            f.xcoords.push(f.x + o * v);
                            f.tspan.textContent += tt;
                            o += w
                        }
                        d ? f.y -= o * v: f.x += o * v;
                        f.tspan.setAttributeNS(null, "x", f.xcoords.map(n).join(" "));
                        f.tspan.setAttributeNS(null, "y", n( - f.y));
                        f.tspan.setAttributeNS(null, "font-family", f.fontFamily);
                        f.tspan.setAttributeNS(null, "font-size", n(f.fontSize) + "px");
                        f.fontStyle !== u.fontStyle && f.tspan.setAttributeNS(null, "font-style", f.fontStyle);
                        f.fontWeight !== u.fontWeight && f.tspan.setAttributeNS(null, "font-weight", f.fontWeight);
                        f.fillColor !== u.fillColor && f.tspan.setAttributeNS(null, "fill", f.fillColor);
                        s = f.textMatrix;
                        f.textRise !== 0 && (s = s.slice(), s[5] += f.textRise);
                        f.txtElement.setAttributeNS(null, "transform", t(s) + " scale(1, -1)");
                        f.txtElement.setAttributeNS(l, "xml:space", "preserve");
                        f.txtElement.appendChild(f.tspan);
                        f.txtgrp.appendChild(f.txtElement);
                        this._ensureTransformGroup().appendChild(f.txtElement)
                    }
                },
                setLeadingMoveText: function(n, t) {
                    this.setLeading( - t);
                    this.moveText(n, t)
                },
                addFontStyle: function(n) {
                    this.cssStyle || (this.cssStyle = this.svgFactory.createElement("svg:style"), this.cssStyle.setAttributeNS(null, "type", "text/css"), this.defs.appendChild(this.cssStyle));
                    var t = r.createObjectURL(n.data, n.mimetype, this.forceDataSchema);
                    this.cssStyle.textContent += '@font-face { font-family: "' + n.loadedName + '"; src: url(' + t + "); }\n"
                },
                setFont: function(t) {
                    var u = this.current,
                    i = this.commonObjs.get(t[0]),
                    f = t[1],
                    e,
                    o;
                    this.current.font = i;
                    this.embedFonts && i.data && !this.embeddedFonts[i.loadedName] && (this.addFontStyle(i), this.embeddedFonts[i.loadedName] = i);
                    u.fontMatrix = i.fontMatrix ? i.fontMatrix: r.FONT_IDENTITY_MATRIX;
                    e = i.black ? i.bold ? "bolder": "bold": i.bold ? "bold": "normal";
                    o = i.italic ? "italic": "normal";
                    f < 0 ? (f = -f, u.fontDirection = -1) : u.fontDirection = 1;
                    u.fontSize = f;
                    u.fontFamily = i.loadedName;
                    u.fontWeight = e;
                    u.fontStyle = o;
                    u.tspan = this.svgFactory.createElement("svg:tspan");
                    u.tspan.setAttributeNS(null, "y", n( - u.y));
                    u.xcoords = []
                },
                endText: function() {},
                setLineWidth: function(n) {
                    this.current.lineWidth = n
                },
                setLineCap: function(n) {
                    this.current.lineCap = a[n]
                },
                setLineJoin: function(n) {
                    this.current.lineJoin = v[n]
                },
                setMiterLimit: function(n) {
                    this.current.miterLimit = n
                },
                setStrokeAlpha: function(n) {
                    this.current.strokeAlpha = n
                },
                setStrokeRGBColor: function(n, t, i) {
                    var u = r.Util.makeCssRgb(n, t, i);
                    this.current.strokeColor = u
                },
                setFillAlpha: function(n) {
                    this.current.fillAlpha = n
                },
                setFillRGBColor: function(n, t, i) {
                    var u = r.Util.makeCssRgb(n, t, i);
                    this.current.fillColor = u;
                    this.current.tspan = this.svgFactory.createElement("svg:tspan");
                    this.current.xcoords = []
                },
                setDash: function(n, t) {
                    this.current.dashArray = n;
                    this.current.dashPhase = t
                },
                constructPath: function(t, i) {
                    var o = this.current,
                    f = o.x,
                    e = o.y,
                    s, c, h, u;
                    for (o.path = this.svgFactory.createElement("svg:path"), s = [], c = t.length, h = 0, u = 0; h < c; h++) switch (t[h] | 0) {
                    case r.OPS.rectangle:
                        f = i[u++];
                        e = i[u++];
                        var v = i[u++],
                        y = i[u++],
                        l = f + v,
                        a = e + y;
                        s.push("M", n(f), n(e), "L", n(l), n(e), "L", n(l), n(a), "L", n(f), n(a), "Z");
                        break;
                    case r.OPS.moveTo:
                        f = i[u++];
                        e = i[u++];
                        s.push("M", n(f), n(e));
                        break;
                    case r.OPS.lineTo:
                        f = i[u++];
                        e = i[u++];
                        s.push("L", n(f), n(e));
                        break;
                    case r.OPS.curveTo:
                        f = i[u + 4];
                        e = i[u + 5];
                        s.push("C", n(i[u]), n(i[u + 1]), n(i[u + 2]), n(i[u + 3]), n(f), n(e));
                        u += 6;
                        break;
                    case r.OPS.curveTo2:
                        f = i[u + 2];
                        e = i[u + 3];
                        s.push("C", n(f), n(e), n(i[u]), n(i[u + 1]), n(i[u + 2]), n(i[u + 3]));
                        u += 4;
                        break;
                    case r.OPS.curveTo3:
                        f = i[u + 2];
                        e = i[u + 3];
                        s.push("C", n(i[u]), n(i[u + 1]), n(f), n(e), n(f), n(e));
                        u += 4;
                        break;
                    case r.OPS.closePath:
                        s.push("Z")
                    }
                    o.path.setAttributeNS(null, "d", s.join(" "));
                    o.path.setAttributeNS(null, "fill", "none");
                    this._ensureTransformGroup().appendChild(o.path);
                    o.element = o.path;
                    o.setCurrentPoint(f, e)
                },
                endPath: function() {
                    var n, u, i, r;
                    this.pendingClip && (n = this.current, u = "clippath" + h, h++, i = this.svgFactory.createElement("svg:clipPath"), i.setAttributeNS(null, "id", u), i.setAttributeNS(null, "transform", t(this.transformMatrix)), r = n.element.cloneNode(), this.pendingClip === "evenodd" ? r.setAttributeNS(null, "clip-rule", "evenodd") : r.setAttributeNS(null, "clip-rule", "nonzero"), this.pendingClip = null, i.appendChild(r), this.defs.appendChild(i), n.activeClipUrl && (n.clipGroup = null, this.extraStack.forEach(function(n) {
                        n.clipGroup = null
                    })), n.activeClipUrl = "url(#" + u + ")", this.tgrp = null)
                },
                clip: function(n) {
                    this.pendingClip = n
                },
                closePath: function() {
                    var n = this.current,
                    t = n.path.getAttributeNS(null, "d");
                    t += "Z";
                    n.path.setAttributeNS(null, "d", t)
                },
                setLeading: function(n) {
                    this.current.leading = -n
                },
                setTextRise: function(n) {
                    this.current.textRise = n
                },
                setHScale: function(n) {
                    this.current.textHScale = n / 100
                },
                setGState: function(n) {
                    for (var i = 0,
                    u = n.length; i < u; i++) {
                        var f = n[i],
                        e = f[0],
                        t = f[1];
                        switch (e) {
                        case "LW":
                            this.setLineWidth(t);
                            break;
                        case "LC":
                            this.setLineCap(t);
                            break;
                        case "LJ":
                            this.setLineJoin(t);
                            break;
                        case "ML":
                            this.setMiterLimit(t);
                            break;
                        case "D":
                            this.setDash(t[0], t[1]);
                            break;
                        case "Font":
                            this.setFont(t);
                            break;
                        case "CA":
                            this.setStrokeAlpha(t);
                            break;
                        case "ca":
                            this.setFillAlpha(t);
                            break;
                        default:
                            r.warn("Unimplemented graphic state " + e)
                        }
                    }
                },
                fill: function() {
                    var n = this.current;
                    n.element.setAttributeNS(null, "fill", n.fillColor);
                    n.element.setAttributeNS(null, "fill-opacity", n.fillAlpha)
                },
                stroke: function() {
                    var t = this.current;
                    t.element.setAttributeNS(null, "stroke", t.strokeColor);
                    t.element.setAttributeNS(null, "stroke-opacity", t.strokeAlpha);
                    t.element.setAttributeNS(null, "stroke-miterlimit", n(t.miterLimit));
                    t.element.setAttributeNS(null, "stroke-linecap", t.lineCap);
                    t.element.setAttributeNS(null, "stroke-linejoin", t.lineJoin);
                    t.element.setAttributeNS(null, "stroke-width", n(t.lineWidth) + "px");
                    t.element.setAttributeNS(null, "stroke-dasharray", t.dashArray.map(n).join(" "));
                    t.element.setAttributeNS(null, "stroke-dashoffset", n(t.dashPhase) + "px");
                    t.element.setAttributeNS(null, "fill", "none")
                },
                eoFill: function() {
                    this.current.element.setAttributeNS(null, "fill-rule", "evenodd");
                    this.fill()
                },
                fillStroke: function() {
                    this.stroke();
                    this.fill()
                },
                eoFillStroke: function() {
                    this.current.element.setAttributeNS(null, "fill-rule", "evenodd");
                    this.fillStroke()
                },
                closeStroke: function() {
                    this.closePath();
                    this.stroke()
                },
                closeFillStroke: function() {
                    this.closePath();
                    this.fillStroke()
                },
                paintSolidColorImageMask: function() {
                    var t = this.current,
                    n = this.svgFactory.createElement("svg:rect");
                    n.setAttributeNS(null, "x", "0");
                    n.setAttributeNS(null, "y", "0");
                    n.setAttributeNS(null, "width", "1px");
                    n.setAttributeNS(null, "height", "1px");
                    n.setAttributeNS(null, "fill", t.fillColor);
                    this._ensureTransformGroup().appendChild(n)
                },
                paintJpegXObject: function(t, i, r) {
                    var e = this.objs.get(t),
                    u = this.svgFactory.createElement("svg:image");
                    u.setAttributeNS(f, "xlink:href", e.src);
                    u.setAttributeNS(null, "width", n(i));
                    u.setAttributeNS(null, "height", n(r));
                    u.setAttributeNS(null, "x", "0");
                    u.setAttributeNS(null, "y", n( - r));
                    u.setAttributeNS(null, "transform", "scale(" + n(1 / i) + " " + n( - 1 / r) + ")");
                    this._ensureTransformGroup().appendChild(u)
                },
                paintImageXObject: function(n) {
                    var t = this.objs.get(n);
                    if (!t) {
                        r.warn("Dependent image isn't ready yet");
                        return
                    }
                    this.paintInlineImageXObject(t)
                },
                paintInlineImageXObject: function(t, i) {
                    var s = t.width,
                    e = t.height,
                    h = o(t, this.forceDataSchema),
                    u = this.svgFactory.createElement("svg:rect"),
                    r;
                    u.setAttributeNS(null, "x", "0");
                    u.setAttributeNS(null, "y", "0");
                    u.setAttributeNS(null, "width", n(s));
                    u.setAttributeNS(null, "height", n(e));
                    this.current.element = u;
                    this.clip("nonzero");
                    r = this.svgFactory.createElement("svg:image");
                    r.setAttributeNS(f, "xlink:href", h);
                    r.setAttributeNS(null, "x", "0");
                    r.setAttributeNS(null, "y", n( - e));
                    r.setAttributeNS(null, "width", n(s) + "px");
                    r.setAttributeNS(null, "height", n(e) + "px");
                    r.setAttributeNS(null, "transform", "scale(" + n(1 / s) + " " + n( - 1 / e) + ")");
                    i ? i.appendChild(r) : this._ensureTransformGroup().appendChild(r)
                },
                paintImageMaskXObject: function(t) {
                    var r = this.current,
                    f = t.width,
                    e = t.height,
                    o = r.fillColor,
                    u, i;
                    r.maskId = "mask" + y++;
                    u = this.svgFactory.createElement("svg:mask");
                    u.setAttributeNS(null, "id", r.maskId);
                    i = this.svgFactory.createElement("svg:rect");
                    i.setAttributeNS(null, "x", "0");
                    i.setAttributeNS(null, "y", "0");
                    i.setAttributeNS(null, "width", n(f));
                    i.setAttributeNS(null, "height", n(e));
                    i.setAttributeNS(null, "fill", o);
                    i.setAttributeNS(null, "mask", "url(#" + r.maskId + ")");
                    this.defs.appendChild(u);
                    this._ensureTransformGroup().appendChild(i);
                    this.paintInlineImageXObject(t, u)
                },
                paintFormXObjectBegin: function(t, i) {
                    if (Array.isArray(t) && t.length === 6 && this.transform(t[0], t[1], t[2], t[3], t[4], t[5]), Array.isArray(i) && i.length === 4) {
                        var u = i[2] - i[0],
                        f = i[3] - i[1],
                        r = this.svgFactory.createElement("svg:rect");
                        r.setAttributeNS(null, "x", i[0]);
                        r.setAttributeNS(null, "y", i[1]);
                        r.setAttributeNS(null, "width", n(u));
                        r.setAttributeNS(null, "height", n(f));
                        this.current.element = r;
                        this.clip("nonzero");
                        this.endPath()
                    }
                },
                paintFormXObjectEnd: function() {},
                _initialize: function(n) {
                    var r = this.svgFactory.create(n.width, n.height),
                    u = this.svgFactory.createElement("svg:defs"),
                    i;
                    return r.appendChild(u),
                    this.defs = u,
                    i = this.svgFactory.createElement("svg:g"),
                    i.setAttributeNS(null, "transform", t(n.transform)),
                    r.appendChild(i),
                    this.svg = i,
                    r
                },
                _ensureClipGroup: function() {
                    if (!this.current.clipGroup) {
                        var n = this.svgFactory.createElement("svg:g");
                        n.setAttributeNS(null, "clip-path", this.current.activeClipUrl);
                        this.svg.appendChild(n);
                        this.current.clipGroup = n
                    }
                    return this.current.clipGroup
                },
                _ensureTransformGroup: function() {
                    return this.tgrp || (this.tgrp = this.svgFactory.createElement("svg:g"), this.tgrp.setAttributeNS(null, "transform", t(this.transformMatrix)), this.current.activeClipUrl ? this._ensureClipGroup().appendChild(this.tgrp) : this.svg.appendChild(this.tgrp)),
                    this.tgrp
                }
            },
            i
        } ();
        t.SVGGraphics = f
    },
    function(n, t, i) {
        "use strict";
        var r = i(0),
        h = i(113),
        u = i(57),
        c = i(61),
        l = i(60),
        f = i(8),
        a = i(62),
        e,
        o,
        s;
        r.isNodeJS() ? (e = i(118).PDFNodeStream, u.setPDFNetworkStreamClass(e)) : typeof Response != "undefined" && "body" in Response.prototype && typeof ReadableStream != "undefined" ? (o = i(119).PDFFetchStream, u.setPDFNetworkStreamClass(o)) : (s = i(120).PDFNetworkStream, u.setPDFNetworkStreamClass(s));
        t.PDFJS = h.PDFJS;
        t.build = u.build;
        t.version = u.version;
        t.getDocument = u.getDocument;
        t.LoopbackPort = u.LoopbackPort;
        t.PDFDataRangeTransport = u.PDFDataRangeTransport;
        t.PDFWorker = u.PDFWorker;
        t.renderTextLayer = c.renderTextLayer;
        t.AnnotationLayer = l.AnnotationLayer;
        t.CustomStyle = f.CustomStyle;
        t.createPromiseCapability = r.createPromiseCapability;
        t.PasswordResponses = r.PasswordResponses;
        t.InvalidPDFException = r.InvalidPDFException;
        t.MissingPDFException = r.MissingPDFException;
        t.SVGGraphics = a.SVGGraphics;
        t.NativeImageDecoding = r.NativeImageDecoding;
        t.UnexpectedResponseException = r.UnexpectedResponseException;
        t.OPS = r.OPS;
        t.UNSUPPORTED_FEATURES = r.UNSUPPORTED_FEATURES;
        t.createValidAbsoluteUrl = r.createValidAbsoluteUrl;
        t.createObjectURL = r.createObjectURL;
        t.removeNullCharacters = r.removeNullCharacters;
        t.shadow = r.shadow;
        t.createBlob = r.createBlob;
        t.RenderingCancelledException = f.RenderingCancelledException;
        t.getFilenameFromUrl = f.getFilenameFromUrl;
        t.addLinkAttributes = f.addLinkAttributes;
        t.StatTimer = r.StatTimer
    },
    function(n, t, i) {
        "use strict";
        var e = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        };
        if (typeof PDFJS == "undefined" || !PDFJS.compatibilityChecked) {
            var r = i(14),
            u = typeof navigator != "undefined" && navigator.userAgent || "",
            s = /Android/.test(u),
            h = /Android\s[0-2][^\d]/.test(u),
            a = /Android\s[0-4][^\d]/.test(u),
            v = u.indexOf("Chrom") >= 0,
            y = /Chrome\/(39|40)\./.test(u),
            p = u.indexOf("CriOS") >= 0,
            c = u.indexOf("Trident") >= 0,
            o = /\b(iPad|iPhone|iPod)(?=;)/.test(u),
            w = u.indexOf("Opera") >= 0,
            l = /Safari\//.test(u) && !/(Chrome\/|Android\s)/.test(u),
            f = (typeof window == "undefined" ? "undefined": e(window)) === "object" && (typeof document == "undefined" ? "undefined": e(document)) === "object";
            typeof PDFJS == "undefined" && (r.PDFJS = {});
            PDFJS.compatibilityChecked = !0,
            function() {
                r.URL || (r.URL = r.webkitURL)
            } (),
            function() {
                var n, t;
                if (typeof Object.defineProperty != "undefined") {
                    n = !0;
                    try {
                        f && Object.defineProperty(new Image, "id", {
                            value: "test"
                        });
                        t = function() {};
                        t.prototype = {
                            get id() {}
                        };
                        Object.defineProperty(new t, "id", {
                            value: "",
                            configurable: !0,
                            enumerable: !0,
                            writable: !1
                        })
                    } catch(i) {
                        n = !1
                    }
                    if (n) return
                }
                Object.defineProperty = function(n, t, i) {
                    delete n[t];
                    "get" in i && n.__defineGetter__(t, i.get);
                    "set" in i && n.__defineSetter__(t, i.set);
                    "value" in i && (n.__defineSetter__(t,
                    function(n) {
                        return this.__defineGetter__(t,
                        function() {
                            return n
                        }),
                        n
                    }), n[t] = i.value)
                }
            } (),
            function() {
                if (typeof XMLHttpRequest != "undefined") {
                    var n = XMLHttpRequest.prototype,
                    t = new XMLHttpRequest;
                    if ("overrideMimeType" in t || Object.defineProperty(n, "overrideMimeType", {
                        value: function() {}
                    }), !("responseType" in t)) {
                        if (Object.defineProperty(n, "responseType", {
                            get: function() {
                                return this._responseType || "text"
                            },
                            set: function(n) { (n === "text" || n === "arraybuffer") && (this._responseType = n, n === "arraybuffer" && typeof this.overrideMimeType == "function" && this.overrideMimeType("text/plain; charset=x-user-defined"))
                            }
                        }), typeof VBArray != "undefined") {
                            Object.defineProperty(n, "response", {
                                get: function() {
                                    return this.responseType === "arraybuffer" ? new Uint8Array(new VBArray(this.responseBody).toArray()) : this.responseText
                                }
                            });
                            return
                        }
                        Object.defineProperty(n, "response", {
                            get: function() {
                                if (this.responseType !== "arraybuffer") return this.responseText;
                                for (var t = this.responseText,
                                i = t.length,
                                r = new Uint8Array(i), n = 0; n < i; ++n) r[n] = t.charCodeAt(n) & 255;
                                return r.buffer
                            }
                        })
                    }
                }
            } (),
            function() {
                if (! ("btoa" in r)) {
                    var n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
                    r.btoa = function(t) {
                        for (var u = "",
                        i = 0,
                        r = t.length; i < r; i += 3) {
                            var f = t.charCodeAt(i) & 255,
                            e = t.charCodeAt(i + 1) & 255,
                            o = t.charCodeAt(i + 2) & 255,
                            s = f >> 2,
                            h = (f & 3) << 4 | e >> 4,
                            c = i + 1 < r ? (e & 15) << 2 | o >> 6 : 64,
                            l = i + 2 < r ? o & 63 : 64;
                            u += n.charAt(s) + n.charAt(h) + n.charAt(c) + n.charAt(l)
                        }
                        return u
                    }
                }
            } (),
            function() {
                if (! ("atob" in r)) {
                    var n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
                    r.atob = function(t) {
                        if (t = t.replace(/=+$/, ""), t.length % 4 == 1) throw new Error("bad atob input");
                        for (var r = 0,
                        u, i, e = 0,
                        f = ""; i = t.charAt(e++);~i && (u = r % 4 ? u * 64 + i: i, r++%4) ? f += String.fromCharCode(255 & u >> ( - 2 * r & 6)) : 0) i = n.indexOf(i);
                        return f
                    }
                }
            } (),
            function() {
                typeof Function.prototype.bind == "undefined" && (Function.prototype.bind = function(n) {
                    var t = this,
                    i = Array.prototype.slice.call(arguments, 1);
                    return function() {
                        var r = i.concat(Array.prototype.slice.call(arguments));
                        return t.apply(n, r)
                    }
                })
            } (),
            function() {
                if (f) {
                    var n = document.createElement("div");
                    "dataset" in n || Object.defineProperty(HTMLElement.prototype, "dataset", {
                        get: function() {
                            var n, t, r, i, u;
                            if (this._dataset) return this._dataset;
                            for (n = {},
                            t = 0, r = this.attributes.length; t < r; t++)(i = this.attributes[t], i.name.substring(0, 5) === "data-") && (u = i.name.substring(5).replace(/\-([a-z])/g,
                            function(n, t) {
                                return t.toUpperCase()
                            }), n[u] = i.value);
                            return Object.defineProperty(this, "_dataset", {
                                value: n,
                                writable: !1,
                                enumerable: !1
                            }),
                            n
                        },
                        enumerable: !0
                    })
                }
            } (),
            function() {
                function n(n, t, i, r) {
                    var e = n.className || "",
                    u = e.split(/\s+/g),
                    f;
                    return u[0] === "" && u.shift(),
                    f = u.indexOf(t),
                    f < 0 && i && u.push(t),
                    f >= 0 && r && u.splice(f, 1),
                    n.className = u.join(" "),
                    f >= 0
                }
                var t, i;
                f && ((t = document.createElement("div"), "classList" in t) || (i = {
                    add: function(t) {
                        n(this.element, t, !0, !1)
                    },
                    contains: function(t) {
                        return n(this.element, t, !1, !1)
                    },
                    remove: function(t) {
                        n(this.element, t, !1, !0)
                    },
                    toggle: function(t) {
                        n(this.element, t, !0, !0)
                    }
                },
                Object.defineProperty(HTMLElement.prototype, "classList", {
                    get: function() {
                        if (this._classList) return this._classList;
                        var n = Object.create(i, {
                            element: {
                                value: this,
                                writable: !1,
                                enumerable: !0
                            }
                        });
                        return Object.defineProperty(this, "_classList", {
                            value: n,
                            writable: !1,
                            enumerable: !1
                        }),
                        n
                    },
                    enumerable: !0
                })))
            } (),
            function() {
                if (typeof importScripts != "undefined" && !("console" in r)) {
                    var n = {},
                    t = {
                        log: function() {
                            var n = Array.prototype.slice.call(arguments);
                            r.postMessage({
                                targetName: "main",
                                action: "console_log",
                                data: n
                            })
                        },
                        error: function() {
                            var n = Array.prototype.slice.call(arguments);
                            r.postMessage({
                                targetName: "main",
                                action: "console_error",
                                data: n
                            })
                        },
                        time: function(t) {
                            n[t] = Date.now()
                        },
                        timeEnd: function(t) {
                            var i = n[t];
                            if (!i) throw new Error("Unknown timer name " + t);
                            this.log("Timer:", t, Date.now() - i)
                        }
                    };
                    r.console = t
                }
            } (),
            function() {
                if (f) {
                    if (! ("console" in window)) {
                        window.console = {
                            log: function() {},
                            error: function() {},
                            warn: function() {}
                        };
                        return
                    }
                    if (! ("bind" in console.log)) {
                        console.log = function(n) {
                            return function(t) {
                                return n(t)
                            }
                        } (console.log);
                        console.error = function(n) {
                            return function(t) {
                                return n(t)
                            }
                        } (console.error);
                        console.warn = function(n) {
                            return function(t) {
                                return n(t)
                            }
                        } (console.warn);
                        return
                    }
                }
            } (),
            function() {
                function t(t) {
                    n(t.target) && t.stopPropagation()
                }
                function n(t) {
                    return t.disabled || t.parentNode && n(t.parentNode)
                }
                w && document.addEventListener("click", t, !0)
            } (),
            function() { (c || p) && (PDFJS.disableCreateObjectURL = !0)
            } (),
            function() {
                typeof navigator != "undefined" && ("language" in navigator || (PDFJS.locale = navigator.userLanguage || "en-US"))
            } (),
            function() { (l || h || y || o) && (PDFJS.disableRange = !0, PDFJS.disableStream = !0)
            } (),
            function() {
                f && (!history.pushState || h) && (PDFJS.disableHistory = !0)
            } (),
            function() {
                var t, n, i, r;
                f && (window.CanvasPixelArray ? typeof window.CanvasPixelArray.prototype.set != "function" && (window.CanvasPixelArray.prototype.set = function(n) {
                    for (var t = 0,
                    i = this.length; t < i; t++) this[t] = n[t]
                }) : (t = !1, v ? (n = u.match(/Chrom(e|ium)\/([0-9]+)\./), t = n && parseInt(n[2]) < 21) : s ? t = a: l && (n = u.match(/Version\/([0-9]+)\.([0-9]+)\.([0-9]+) Safari\//), t = n && parseInt(n[1]) < 6), t && (i = window.CanvasRenderingContext2D.prototype, r = i.createImageData, i.createImageData = function(n, t) {
                    var i = r.call(this, n, t);
                    return i.data.set = function(n) {
                        for (var t = 0,
                        i = this.length; t < i; t++) this[t] = n[t]
                    },
                    i
                },
                i = null)))
            } (),
            function() {
                function n() {
                    window.requestAnimationFrame = function(n) {
                        return window.setTimeout(n, 20)
                    };
                    window.cancelAnimationFrame = function(n) {
                        window.clearTimeout(n)
                    }
                }
                if (f) {
                    if (o) {
                        n();
                        return
                    }
                    "requestAnimationFrame" in window || (window.requestAnimationFrame = window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame, window.requestAnimationFrame) || n()
                }
            } (),
            function() { (o || s) && (PDFJS.maxCanvasPixels = 5242880)
            } (),
            function() {
                f && c && window.parent !== window && (PDFJS.disableFullscreen = !0)
            } (),
            function() {
                f && ("currentScript" in document || Object.defineProperty(document, "currentScript", {
                    get: function() {
                        var n = document.getElementsByTagName("script");
                        return n[n.length - 1]
                    },
                    enumerable: !0,
                    configurable: !0
                }))
            } (),
            function() {
                var n, t, i;
                if (f) {
                    n = document.createElement("input");
                    try {
                        n.type = "number"
                    } catch(r) {
                        t = n.constructor.prototype;
                        i = Object.getOwnPropertyDescriptor(t, "type");
                        Object.defineProperty(t, "type", {
                            get: function() {
                                return i.get.call(this)
                            },
                            set: function(n) {
                                i.set.call(this, n === "number" ? "text": n)
                            },
                            enumerable: !0,
                            configurable: !0
                        })
                    }
                }
            } (),
            function() {
                if (f && document.attachEvent) {
                    var n = document.constructor.prototype,
                    t = Object.getOwnPropertyDescriptor(n, "readyState");
                    Object.defineProperty(n, "readyState", {
                        get: function() {
                            var n = t.get.call(this);
                            return n === "interactive" ? "loading": n
                        },
                        set: function(n) {
                            t.set.call(this, n)
                        },
                        enumerable: !0,
                        configurable: !0
                    })
                }
            } (),
            function() {
                f && typeof Element.prototype.remove == "undefined" && (Element.prototype.remove = function() {
                    this.parentNode && this.parentNode.removeChild(this)
                })
            } (),
            function() {
                Object.values || (Object.values = i(65))
            } (),
            function() {
                Array.prototype.includes || (Array.prototype.includes = i(70))
            } (),
            function() {
                Number.isNaN || (Number.isNaN = i(72))
            } (),
            function() {
                Number.isInteger || (Number.isInteger = i(74))
            } (),
            function() {
                r.Promise || (r.Promise = i(77))
            } (),
            function() {
                r.WeakMap || (r.WeakMap = i(95))
            } (),
            function() {
                function p(n) {
                    return t[n] !== undefined
                }
                function c() {
                    v.call(this);
                    this._isInvalid = !0
                }
                function l(n) {
                    return n === "" && c.call(this),
                    n.toLowerCase()
                }
                function a(n) {
                    var t = n.charCodeAt(0);
                    return t > 32 && t < 127 && [34, 35, 60, 62, 63, 96].indexOf(t) === -1 ? n: encodeURIComponent(n)
                }
                function b(n) {
                    var t = n.charCodeAt(0);
                    return t > 32 && t < 127 && [34, 35, 60, 62, 96].indexOf(t) === -1 ? n: encodeURIComponent(n)
                }
                function i(i, r, u) {
                    function v(n) {
                        et.push(n)
                    }
                    var h = r || "scheme start",
                    y = 0,
                    s = "",
                    ut = !1,
                    nt = !1,
                    et = [],
                    e,
                    tt,
                    k,
                    g,
                    d,
                    it,
                    rt,
                    ft;
                    n: while ((i[y - 1] !== n || y === 0) && !this._isInvalid) {
                        e = i[y];
                        switch (h) {
                        case "scheme start":
                            if (e && o.test(e)) s += e.toLowerCase(),
                            h = "scheme";
                            else if (r) {
                                v("Invalid scheme.");
                                break n
                            } else {
                                s = "";
                                h = "no scheme";
                                continue
                            }
                            break;
                        case "scheme":
                            if (e && w.test(e)) s += e.toLowerCase();
                            else if (e === ":") {
                                if (this._scheme = s, s = "", r) break n;
                                p(this._scheme) && (this._isRelative = !0);
                                h = this._scheme === "file" ? "relative": this._isRelative && u && u._scheme === this._scheme ? "relative or authority": this._isRelative ? "authority first slash": "scheme data"
                            } else if (r) if (e === n) break n;
                            else {
                                v("Code point not allowed in scheme: " + e);
                                break n
                            } else {
                                s = "";
                                y = 0;
                                h = "no scheme";
                                continue
                            }
                            break;
                        case "scheme data":
                            e === "?" ? (this._query = "?", h = "query") : e === "#" ? (this._fragment = "#", h = "fragment") : e !== n && e !== "\t" && e !== "\n" && e !== "\r" && (this._schemeData += a(e));
                            break;
                        case "no scheme":
                            if (u && p(u._scheme)) {
                                h = "relative";
                                continue
                            } else v("Missing scheme."),
                            c.call(this);
                            break;
                        case "relative or authority":
                            if (e === "/" && i[y + 1] === "/") h = "authority ignore slashes";
                            else {
                                v("Expected /, got: " + e);
                                h = "relative";
                                continue
                            }
                            break;
                        case "relative":
                            if (this._isRelative = !0, this._scheme !== "file" && (this._scheme = u._scheme), e === n) {
                                this._host = u._host;
                                this._port = u._port;
                                this._path = u._path.slice();
                                this._query = u._query;
                                this._username = u._username;
                                this._password = u._password;
                                break n
                            } else if (e === "/" || e === "\\") e === "\\" && v("\\ is an invalid code point."),
                            h = "relative slash";
                            else if (e === "?") this._host = u._host,
                            this._port = u._port,
                            this._path = u._path.slice(),
                            this._query = "?",
                            this._username = u._username,
                            this._password = u._password,
                            h = "query";
                            else if (e === "#") this._host = u._host,
                            this._port = u._port,
                            this._path = u._path.slice(),
                            this._query = u._query,
                            this._fragment = "#",
                            this._username = u._username,
                            this._password = u._password,
                            h = "fragment";
                            else {
                                tt = i[y + 1];
                                k = i[y + 2];
                                this._scheme === "file" && o.test(e) && (tt === ":" || tt === "|") && (k === n || k === "/" || k === "\\" || k === "?" || k === "#") || (this._host = u._host, this._port = u._port, this._username = u._username, this._password = u._password, this._path = u._path.slice(), this._path.pop());
                                h = "relative path";
                                continue
                            }
                            break;
                        case "relative slash":
                            if (e === "/" || e === "\\") e === "\\" && v("\\ is an invalid code point."),
                            h = this._scheme === "file" ? "file host": "authority ignore slashes";
                            else {
                                this._scheme !== "file" && (this._host = u._host, this._port = u._port, this._username = u._username, this._password = u._password);
                                h = "relative path";
                                continue
                            }
                            break;
                        case "authority first slash":
                            if (e === "/") h = "authority second slash";
                            else {
                                v("Expected '/', got: " + e);
                                h = "authority ignore slashes";
                                continue
                            }
                            break;
                        case "authority second slash":
                            if (h = "authority ignore slashes", e !== "/") {
                                v("Expected '/', got: " + e);
                                continue
                            }
                            break;
                        case "authority ignore slashes":
                            if (e !== "/" && e !== "\\") {
                                h = "authority";
                                continue
                            } else v("Expected authority, got: " + e);
                            break;
                        case "authority":
                            if (e === "@") {
                                for (ut && (v("@ already seen."), s += "%40"), ut = !0, g = 0; g < s.length; g++) {
                                    if (d = s[g], d === "\t" || d === "\n" || d === "\r") {
                                        v("Invalid whitespace in authority.");
                                        continue
                                    }
                                    if (d === ":" && this._password === null) {
                                        this._password = "";
                                        continue
                                    }
                                    it = a(d);
                                    this._password !== null ? this._password += it: this._username += it
                                }
                                s = ""
                            } else if (e === n || e === "/" || e === "\\" || e === "?" || e === "#") {
                                y -= s.length;
                                s = "";
                                h = "host";
                                continue
                            } else s += e;
                            break;
                        case "file host":
                            if (e === n || e === "/" || e === "\\" || e === "?" || e === "#") {
                                s.length === 2 && o.test(s[0]) && (s[1] === ":" || s[1] === "|") ? h = "relative path": s.length === 0 ? h = "relative path start": (this._host = l.call(this, s), s = "", h = "relative path start");
                                continue
                            } else e === "\t" || e === "\n" || e === "\r" ? v("Invalid whitespace in file host.") : s += e;
                            break;
                        case "host":
                        case "hostname":
                            if (e !== ":" || nt) if (e === n || e === "/" || e === "\\" || e === "?" || e === "#") {
                                if (this._host = l.call(this, s), s = "", h = "relative path start", r) break n;
                                continue
                            } else e !== "\t" && e !== "\n" && e !== "\r" ? (e === "[" ? nt = !0 : e === "]" && (nt = !1), s += e) : v("Invalid code point in host/hostname: " + e);
                            else if (this._host = l.call(this, s), s = "", h = "port", r === "hostname") break n;
                            break;
                        case "port":
                            if (/[0-9]/.test(e)) s += e;
                            else if (e === n || e === "/" || e === "\\" || e === "?" || e === "#" || r) {
                                if (s !== "" && (rt = parseInt(s, 10), rt !== t[this._scheme] && (this._port = rt + ""), s = ""), r) break n;
                                h = "relative path start";
                                continue
                            } else e === "\t" || e === "\n" || e === "\r" ? v("Invalid code point in port: " + e) : c.call(this);
                            break;
                        case "relative path start":
                            if (e === "\\" && v("'\\' not allowed in path."), h = "relative path", e !== "/" && e !== "\\") continue;
                            break;
                        case "relative path":
                            e !== n && e !== "/" && e !== "\\" && (r || e !== "?" && e !== "#") ? e !== "\t" && e !== "\n" && e !== "\r" && (s += a(e)) : (e === "\\" && v("\\ not allowed in relative path."), (ft = f[s.toLowerCase()]) && (s = ft), s === ".." ? (this._path.pop(), e !== "/" && e !== "\\" && this._path.push("")) : s === "." && e !== "/" && e !== "\\" ? this._path.push("") : s !== "." && (this._scheme === "file" && this._path.length === 0 && s.length === 2 && o.test(s[0]) && s[1] === "|" && (s = s[0] + ":"), this._path.push(s)), s = "", e === "?" ? (this._query = "?", h = "query") : e === "#" && (this._fragment = "#", h = "fragment"));
                            break;
                        case "query":
                            r || e !== "#" ? e !== n && e !== "\t" && e !== "\n" && e !== "\r" && (this._query += b(e)) : (this._fragment = "#", h = "fragment");
                            break;
                        case "fragment":
                            e !== n && e !== "\t" && e !== "\n" && e !== "\r" && (this._fragment += e)
                        }
                        y++
                    }
                }
                function v() {
                    this._scheme = "";
                    this._schemeData = "";
                    this._username = "";
                    this._password = null;
                    this._host = "";
                    this._port = "";
                    this._path = [];
                    this._query = "";
                    this._fragment = "";
                    this._isInvalid = !1;
                    this._isRelative = !1
                }
                function u(n, t) {
                    t === undefined || t instanceof u || (t = new u(String(t)));
                    this._url = n;
                    v.call(this);
                    var r = n.replace(/^[ \t\r\n\f]+|[ \t\r\n\f]+$/g, "");
                    i.call(this, r, null, t)
                }
                var y = !1,
                h, t, f, n, o, w, s;
                try {
                    typeof URL == "function" && e(URL.prototype) === "object" && "origin" in URL.prototype && (h = new URL("b", "http://a"), h.pathname = "c%20d", y = h.href === "http://a/c%20d")
                } catch(k) {}
                y || (t = Object.create(null), t.ftp = 21, t.file = 0, t.gopher = 70, t.http = 80, t.https = 443, t.ws = 80, t.wss = 443, f = Object.create(null), f["%2e"] = ".", f[".%2e"] = "..", f["%2e."] = "..", f["%2e%2e"] = "..", o = /[a-zA-Z]/, w = /[a-zA-Z0-9\+\-\.]/, u.prototype = {
                    toString: function() {
                        return this.href
                    },
                    get href() {
                        if (this._isInvalid) return this._url;
                        var n = "";
                        return (this._username !== "" || this._password !== null) && (n = this._username + (this._password !== null ? ":" + this._password: "") + "@"),
                        this.protocol + (this._isRelative ? "//" + n + this.host: "") + this.pathname + this._query + this._fragment
                    },
                    set href(n) {
                        v.call(this);
                        i.call(this, n)
                    },
                    get protocol() {
                        return this._scheme + ":"
                    },
                    set protocol(n) {
                        this._isInvalid || i.call(this, n + ":", "scheme start")
                    },
                    get host() {
                        return this._isInvalid ? "": this._port ? this._host + ":" + this._port: this._host
                    },
                    set host(n) { ! this._isInvalid && this._isRelative && i.call(this, n, "host")
                    },
                    get hostname() {
                        return this._host
                    },
                    set hostname(n) { ! this._isInvalid && this._isRelative && i.call(this, n, "hostname")
                    },
                    get port() {
                        return this._port
                    },
                    set port(n) { ! this._isInvalid && this._isRelative && i.call(this, n, "port")
                    },
                    get pathname() {
                        return this._isInvalid ? "": this._isRelative ? "/" + this._path.join("/") : this._schemeData
                    },
                    set pathname(n) { ! this._isInvalid && this._isRelative && (this._path = [], i.call(this, n, "relative path start"))
                    },
                    get search() {
                        return this._isInvalid || !this._query || this._query === "?" ? "": this._query
                    },
                    set search(n) { ! this._isInvalid && this._isRelative && (this._query = "?", n[0] === "?" && (n = n.slice(1)), i.call(this, n, "query"))
                    },
                    get hash() {
                        return this._isInvalid || !this._fragment || this._fragment === "#" ? "": this._fragment
                    },
                    set hash(n) {
                        this._isInvalid || (this._fragment = "#", n[0] === "#" && (n = n.slice(1)), i.call(this, n, "fragment"))
                    },
                    get origin() {
                        var n;
                        if (this._isInvalid || !this._scheme) return "";
                        switch (this._scheme) {
                        case "data":
                        case "file":
                        case "javascript":
                        case "mailto":
                            return "null";
                        case "blob":
                            try {
                                return new u(this._schemeData).origin || "null"
                            } catch(t) {}
                            return "null"
                        }
                        return (n = this.host, !n) ? "": this._scheme + "://" + n
                    }
                },
                s = r.URL, s && (u.createObjectURL = function() {
                    return s.createObjectURL.apply(s, arguments)
                },
                u.revokeObjectURL = function(n) {
                    s.revokeObjectURL(n)
                }), r.URL = u)
            } ()
        }
    },
    function(n, t, i) {
        "use strict";
        i(66);
        n.exports = i(5).Object.values
    },
    function(n, t, i) {
        "use strict";
        var r = i(4),
        u = i(67)(!1);
        r(r.S, "Object", {
            values: function(n) {
                return u(n)
            }
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(21),
        u = i(17),
        f = i(31).f;
        n.exports = function(n) {
            return function(t) {
                for (var i = u(t), o = r(i), c = o.length, s = 0, h = [], e; c > s;) f.call(i, e = o[s++]) && h.push(n ? [e, i[e]] : i[e]);
                return h
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(7),
        u = i(17),
        f = i(41)(!1),
        e = i(30)("IE_PROTO");
        n.exports = function(n, t) {
            var s = u(n),
            h = 0,
            o = [];
            for (var i in s) i != e && r(s, i) && o.push(i);
            while (t.length > h) r(s, i = t[h++]) && (~f(o, i) || o.push(i));
            return o
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(29),
        u = Math.max,
        f = Math.min;
        n.exports = function(n, t) {
            return n = r(n),
            n < 0 ? u(n + t, 0) : f(n, t)
        }
    },
    function(n, t, i) {
        "use strict";
        i(71);
        n.exports = i(5).Array.includes
    },
    function(n, t, i) {
        "use strict";
        var r = i(4),
        u = i(41)(!0);
        r(r.P, "Array", {
            includes: function(n) {
                return u(this, n, arguments.length > 1 ? arguments[1] : undefined)
            }
        });
        i(44)("includes")
    },
    function(n, t, i) {
        "use strict";
        i(73);
        n.exports = i(5).Number.isNaN
    },
    function(n, t, i) {
        "use strict";
        var r = i(4);
        r(r.S, "Number", {
            isNaN: function(n) {
                return n != n
            }
        })
    },
    function(n, t, i) {
        "use strict";
        i(75);
        n.exports = i(5).Number.isInteger
    },
    function(n, t, i) {
        "use strict";
        var r = i(4);
        r(r.S, "Number", {
            isInteger: i(76)
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(1),
        u = Math.floor;
        n.exports = function(n) {
            return ! r(n) && isFinite(n) && u(n) === n
        }
    },
    function(n, t, i) {
        "use strict";
        i(45);
        i(78);
        i(49);
        i(86);
        i(93);
        i(94);
        n.exports = i(5).Promise
    },
    function(n, t, i) {
        "use strict";
        var r = i(79)(!0);
        i(46)(String, "String",
        function(n) {
            this._t = String(n);
            this._i = 0
        },
        function() {
            var t = this._t,
            i = this._i,
            n;
            return i >= t.length ? {
                value: undefined,
                done: !0
            }: (n = r(t, i), this._i += n.length, {
                value: n,
                done: !1
            })
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(29),
        u = i(27);
        n.exports = function(n) {
            return function(t, i) {
                var e = String(u(t)),
                f = r(i),
                h = e.length,
                o,
                s;
                return f < 0 || f >= h ? n ? "": undefined: (o = e.charCodeAt(f), o < 55296 || o > 56319 || f + 1 === h || (s = e.charCodeAt(f + 1)) < 56320 || s > 57343 ? n ? e.charAt(f) : o: n ? e.slice(f, f + 2) : (o - 55296 << 10) + (s - 56320) + 65536)
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(81),
        f = i(25),
        e = i(22),
        r = {};
        i(11)(r, i(2)("iterator"),
        function() {
            return this
        });
        n.exports = function(n, t, i) {
            n.prototype = u(r, {
                next: f(1, i)
            });
            e(n, t + " Iterator")
        }
    },
    function(n, t, i) {
        "use strict";
        var o = i(6),
        s = i(82),
        e = i(43),
        h = i(30)("IE_PROTO"),
        u = function() {},
        f = "prototype",
        r = function() {
            var t = i(24)("iframe"),
            u = e.length,
            o = "<",
            s = ">",
            n;
            for (t.style.display = "none", i(48).appendChild(t), t.src = "javascript:", n = t.contentWindow.document, n.open(), n.write(o + "script" + s + "document.F=Object" + o + "/script" + s), n.close(), r = n.F; u--;) delete r[f][e[u]];
            return r()
        };
        n.exports = Object.create ||
        function(n, t) {
            var i;
            return n !== null ? (u[f] = o(n), i = new u, u[f] = null, i[h] = n) : i = r(),
            t === undefined ? i: s(i, t)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(15),
        u = i(6),
        f = i(21);
        n.exports = i(12) ? Object.defineProperties: function(n, t) {
            u(n);
            for (var i = f(t), s = i.length, e = 0, o; s > e;) r.f(n, o = i[e++], t[o]);
            return n
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(7),
        f = i(33),
        r = i(30)("IE_PROTO"),
        e = Object.prototype;
        n.exports = Object.getPrototypeOf ||
        function(n) {
            return (n = f(n), u(n, r)) ? n[r] : typeof n.constructor == "function" && n instanceof n.constructor ? n.constructor.prototype: n instanceof Object ? e: null
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(44),
        r = i(85),
        f = i(19),
        e = i(17);
        n.exports = i(46)(Array, "Array",
        function(n, t) {
            this._t = e(n);
            this._i = 0;
            this._k = t
        },
        function() {
            var t = this._t,
            i = this._k,
            n = this._i++;
            return ! t || n >= t.length ? (this._t = undefined, r(1)) : i == "keys" ? r(0, n) : i == "values" ? r(0, t[n]) : r(0, [n, t[n]])
        },
        "values");
        f.Arguments = f.Array;
        u("keys");
        u("values");
        u("entries")
    },
    function(n) {
        "use strict";
        n.exports = function(n, t) {
            return {
                value: t,
                done: !!n
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var g = i(47),
        f = i(3),
        o = i(10),
        st = i(32),
        r = i(4),
        ht = i(1),
        ct = i(16),
        lt = i(34),
        nt = i(23),
        at = i(50),
        tt = i(51).set,
        it = i(91)(),
        rt = i(35),
        p = i(52),
        vt = i(53),
        e = "Promise",
        ut = f.TypeError,
        a = f.process,
        u = f[e],
        h = st(a) == "process",
        v = function() {},
        y,
        ft,
        et,
        w,
        c = ft = rt.f,
        l = !!
        function() {
            try {
                var n = u.resolve(1),
                t = (n.constructor = {})[i(2)("species")] = function(n) {
                    n(v, v)
                };
                return (h || typeof PromiseRejectionEvent == "function") && n.then(v) instanceof t
            } catch(r) {}
        } (),
        ot = function(n) {
            var t;
            return ht(n) && typeof(t = n.then) == "function" ? t: !1
        },
        b = function(n, t) {
            if (!n._n) {
                n._n = !0;
                var i = n._c;
                it(function() {
                    for (var r = n._v,
                    u = n._s == 1,
                    f = 0,
                    e = function(t) {
                        var o = u ? t.ok: t.fail,
                        s = t.resolve,
                        f = t.reject,
                        e = t.domain,
                        i,
                        h;
                        try {
                            o ? (u || (n._h == 2 && pt(n), n._h = 1), o === !0 ? i = r: (e && e.enter(), i = o(r), e && e.exit()), i === t.promise ? f(ut("Promise-chain cycle")) : (h = ot(i)) ? h.call(i, s, f) : s(i)) : f(r)
                        } catch(c) {
                            f(c)
                        }
                    }; i.length > f;) e(i[f++]);
                    n._c = [];
                    n._n = !1;
                    t && !n._h && yt(n)
                })
            }
        },
        yt = function(n) {
            tt.call(f,
            function() {
                var t = n._v,
                u = k(n),
                i,
                e,
                r;
                if (u && (i = p(function() {
                    h ? a.emit("unhandledRejection", t, n) : (e = f.onunhandledrejection) ? e({
                        promise: n,
                        reason: t
                    }) : (r = f.console) && r.error && r.error("Unhandled promise rejection", t)
                }), n._h = h || k(n) ? 2 : 1), n._a = undefined, u && i.e) throw i.v;
            })
        },
        k = function k(n) {
            if (n._h == 1) return ! 1;
            for (var i = n._a || n._c,
            r = 0,
            t; i.length > r;) if (t = i[r++], t.fail || !k(t.promise)) return ! 1;
            return ! 0
        },
        pt = function(n) {
            tt.call(f,
            function() {
                var t;
                h ? a.emit("rejectionHandled", n) : (t = f.onrejectionhandled) && t({
                    promise: n,
                    reason: n._v
                })
            })
        },
        s = function(n) {
            var t = this;
            t._d || (t._d = !0, t = t._w || t, t._v = n, t._s = 2, t._a || (t._a = t._c.slice()), b(t, !0))
        },
        d = function d(n) {
            var t = this,
            i;
            if (!t._d) {
                t._d = !0;
                t = t._w || t;
                try {
                    if (t === n) throw ut("Promise can't be resolved itself"); (i = ot(n)) ? it(function() {
                        var r = {
                            _w: t,
                            _d: !1
                        };
                        try {
                            i.call(n, o(d, r, 1), o(s, r, 1))
                        } catch(u) {
                            s.call(r, u)
                        }
                    }) : (t._v = n, t._s = 1, b(t, !1))
                } catch(r) {
                    s.call({
                        _w: t,
                        _d: !1
                    },
                    r)
                }
            }
        };
        l || (u = function(n) {
            lt(this, u, e, "_h");
            ct(n);
            y.call(this);
            try {
                n(o(d, this, 1), o(s, this, 1))
            } catch(t) {
                s.call(this, t)
            }
        },
        y = function() {
            this._c = [];
            this._a = undefined;
            this._s = 0;
            this._d = !1;
            this._v = undefined;
            this._h = 0;
            this._n = !1
        },
        y.prototype = i(36)(u.prototype, {
            then: function(n, t) {
                var i = c(at(this, u));
                return i.ok = typeof n == "function" ? n: !0,
                i.fail = typeof t == "function" && t,
                i.domain = h ? a.domain: undefined,
                this._c.push(i),
                this._a && this._a.push(i),
                this._s && b(this, !1),
                i.promise
            },
            "catch": function(n) {
                return this.then(undefined, n)
            }
        }), et = function() {
            var n = new y;
            this.promise = n;
            this.resolve = o(d, n, 1);
            this.reject = o(s, n, 1)
        },
        rt.f = c = function(n) {
            return n === u || n === w ? new et(n) : ft(n)
        });
        r(r.G + r.W + r.F * !l, {
            Promise: u
        });
        i(22)(u, e);
        i(92)(e);
        w = i(5)[e];
        r(r.S + r.F * !l, e, {
            reject: function(n) {
                var t = c(this),
                i = t.reject;
                return i(n),
                t.promise
            }
        });
        r(r.S + r.F * (g || !l), e, {
            resolve: function(n) {
                return vt(g && this === w ? u: this, n)
            }
        });
        r(r.S + r.F * !(l && i(54)(function(n) {
            u.all(n)["catch"](v)
        })), e, {
            all: function(n) {
                var i = this,
                t = c(i),
                r = t.resolve,
                u = t.reject,
                f = p(function() {
                    var t = [],
                    e = 0,
                    f = 1;
                    nt(n, !1,
                    function(n) {
                        var s = e++,
                        o = !1;
                        t.push(undefined);
                        f++;
                        i.resolve(n).then(function(n) {
                            o || (o = !0, t[s] = n, --f || r(t))
                        },
                        u)
                    }); --f || r(t)
                });
                return f.e && u(f.v),
                t.promise
            },
            race: function(n) {
                var i = this,
                t = c(i),
                r = t.reject,
                u = p(function() {
                    nt(n, !1,
                    function(n) {
                        i.resolve(n).then(t.resolve, r)
                    })
                });
                return u.e && r(u.v),
                t.promise
            }
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(6);
        n.exports = function(n, t, i, u) {
            try {
                return u ? t(r(i)[0], i[1]) : t(i)
            } catch(e) {
                var f = n["return"];
                f !== undefined && r(f.call(n));
                throw e;
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(19),
        u = i(2)("iterator"),
        f = Array.prototype;
        n.exports = function(n) {
            return n !== undefined && (r.Array === n || f[u] === n)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(32),
        u = i(2)("iterator"),
        f = i(19);
        n.exports = i(5).getIteratorMethod = function(n) {
            if (n != undefined) return n[u] || n["@@iterator"] || f[r(n)]
        }
    },
    function(n) {
        "use strict";
        n.exports = function(n, t, i) {
            var r = i === undefined;
            switch (t.length) {
            case 0:
                return r ? n() : n.call(i);
            case 1:
                return r ? n(t[0]) : n.call(i, t[0]);
            case 2:
                return r ? n(t[0], t[1]) : n.call(i, t[0], t[1]);
            case 3:
                return r ? n(t[0], t[1], t[2]) : n.call(i, t[0], t[1], t[2]);
            case 4:
                return r ? n(t[0], t[1], t[2], t[3]) : n.call(i, t[0], t[1], t[2], t[3])
            }
            return n.apply(i, t)
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(3),
        s = i(51).set,
        e = r.MutationObserver || r.WebKitMutationObserver,
        u = r.process,
        f = r.Promise,
        o = i(18)(u) == "process";
        n.exports = function() {
            var n, i, t, h = function() {
                var r, f;
                for (o && (r = u.domain) && r.exit(); n;) {
                    f = n.fn;
                    n = n.next;
                    try {
                        f()
                    } catch(e) {
                        n ? t() : i = undefined;
                        throw e;
                    }
                }
                i = undefined;
                r && r.enter()
            },
            c,
            l,
            a;
            return o ? t = function() {
                u.nextTick(h)
            }: e ? (c = !0, l = document.createTextNode(""), new e(h).observe(l, {
                characterData: !0
            }), t = function() {
                l.data = c = !c
            }) : f && f.resolve ? (a = f.resolve(), t = function() {
                a.then(h)
            }) : t = function() {
                s.call(r, h)
            },
            function(r) {
                var u = {
                    fn: r,
                    next: undefined
                };
                i && (i.next = u);
                n || (n = u, t());
                i = u
            }
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(3),
        f = i(15),
        e = i(12),
        r = i(2)("species");
        n.exports = function(n) {
            var t = u[n];
            e && t && !t[r] && f.f(t, r, {
                configurable: !0,
                get: function() {
                    return this
                }
            })
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(4),
        f = i(5),
        e = i(3),
        o = i(50),
        u = i(53);
        r(r.P + r.R, "Promise", {
            "finally": function(n) {
                var t = o(this, f.Promise || e.Promise),
                i = typeof n == "function";
                return this.then(i ?
                function(i) {
                    return u(t, n()).then(function() {
                        return i
                    })
                }: n, i ?
                function(i) {
                    return u(t, n()).then(function() {
                        throw i;
                    })
                }: n)
            }
        })
    },
    function(n, t, i) {
        "use strict";
        var r = i(4),
        u = i(35),
        f = i(52);
        r(r.S, "Promise", {
            "try": function(n) {
                var t = u.f(this),
                i = f(n);
                return (i.e ? t.reject: t.resolve)(i.v),
                t.promise
            }
        })
    },
    function(n, t, i) {
        "use strict";
        i(45);
        i(49);
        i(96);
        i(107);
        i(109);
        n.exports = i(5).WeakMap
    },
    function(n, t, i) {
        "use strict";
        var v = i(55)(0),
        y = i(9),
        e = i(37),
        p = i(100),
        r = i(102),
        o = i(1),
        w = i(13),
        s = i(56),
        u = "WeakMap",
        b = e.getWeak,
        k = Object.isExtensible,
        d = r.ufstore,
        h = {},
        f,
        c = function(n) {
            return function() {
                return n(this, arguments.length > 0 ? arguments[0] : undefined)
            }
        },
        l = {
            get: function(n) {
                if (o(n)) {
                    var t = b(n);
                    return t === !0 ? d(s(this, u)).get(n) : t ? t[this._i] : undefined
                }
            },
            set: function(n, t) {
                return r.def(s(this, u), n, t)
            }
        },
        a = n.exports = i(103)(u, c, l, r, !0, !0);
        w(function() {
            return (new a).set((Object.freeze || Object)(h), 7).get(h) != 7
        }) && (f = r.getConstructor(c, u), p(f.prototype, l), e.NEED = !0, v(["delete", "has", "get", "set"],
        function(n) {
            var t = a.prototype,
            i = t[n];
            y(t, n,
            function(t, r) {
                if (o(t) && !k(t)) {
                    this._f || (this._f = new f);
                    var u = this._f[n](t, r);
                    return n == "set" ? this: u
                }
                return i.call(this, t, r)
            })
        }))
    },
    function(n, t, i) {
        "use strict";
        var r = i(98);
        n.exports = function(n, t) {
            return new(r(n))(t)
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(1),
        r = i(99),
        f = i(2)("species");
        n.exports = function(n) {
            var t;
            return r(n) && (t = n.constructor, typeof t == "function" && (t === Array || r(t.prototype)) && (t = undefined), u(t) && (t = t[f], t === null && (t = undefined))),
            t === undefined ? Array: t
        }
    },
    function(n, t, i) {
        "use strict";
        var r = i(18);
        n.exports = Array.isArray ||
        function(n) {
            return r(n) == "Array"
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(21),
        f = i(101),
        e = i(31),
        o = i(33),
        s = i(26),
        r = Object.assign;
        n.exports = !r || i(13)(function() {
            var n = {},
            t = {},
            i = Symbol(),
            u = "abcdefghijklmnopqrst";
            return n[i] = 7,
            u.split("").forEach(function(n) {
                t[n] = n
            }),
            r({},
            n)[i] != 7 || Object.keys(r({},
            t)).join("") != u
        }) ?
        function(n) {
            for (var r = o(n), v = arguments.length, h = 1, c = f.f, y = e.f; v > h;) for (var t = s(arguments[h++]), l = c ? u(t).concat(c(t)) : u(t), p = l.length, a = 0, i; p > a;) y.call(t, i = l[a++]) && (r[i] = t[i]);
            return r
        }: r
    },
    function(n, t) {
        "use strict";
        t.f = Object.getOwnPropertySymbols
    },
    function(n, t, i) {
        "use strict";
        var l = i(36),
        u = i(37).getWeak,
        a = i(6),
        e = i(1),
        v = i(34),
        y = i(23),
        o = i(55),
        s = i(7),
        h = i(56),
        p = o(5),
        w = o(6),
        b = 0,
        r = function(n) {
            return n._l || (n._l = new c)
        },
        c = function() {
            this.a = []
        },
        f = function(n, t) {
            return p(n.a,
            function(n) {
                return n[0] === t
            })
        };
        c.prototype = {
            get: function(n) {
                var t = f(this, n);
                if (t) return t[1]
            },
            has: function(n) {
                return !! f(this, n)
            },
            set: function(n, t) {
                var i = f(this, n);
                i ? i[1] = t: this.a.push([n, t])
            },
            "delete": function(n) {
                var t = w(this.a,
                function(t) {
                    return t[0] === n
                });
                return~t && this.a.splice(t, 1),
                !!~t
            }
        };
        n.exports = {
            getConstructor: function(n, t, i, f) {
                var o = n(function(n, r) {
                    v(n, o, t, "_i");
                    n._t = t;
                    n._i = b++;
                    n._l = undefined;
                    r != undefined && y(r, i, n[f], n)
                });
                return l(o.prototype, {
                    "delete": function(n) {
                        if (!e(n)) return ! 1;
                        var i = u(n);
                        return i === !0 ? r(h(this, t))["delete"](n) : i && s(i, this._i) && delete i[this._i]
                    },
                    has: function(n) {
                        if (!e(n)) return ! 1;
                        var i = u(n);
                        return i === !0 ? r(h(this, t)).has(n) : i && s(i, this._i)
                    }
                }),
                o
            },
            def: function(n, t, i) {
                var f = u(a(t), !0);
                return f === !0 ? r(n).set(t, i) : f[n._i] = i,
                n
            },
            ufstore: r
        }
    },
    function(n, t, i) {
        "use strict";
        var e = i(3),
        r = i(4),
        o = i(9),
        s = i(36),
        h = i(37),
        c = i(23),
        l = i(34),
        u = i(1),
        f = i(13),
        a = i(54),
        v = i(22),
        y = i(104);
        n.exports = function(n, t, i, p, w, b) {
            var tt = e[n],
            k = tt,
            g = w ? "set": "add",
            d = k && k.prototype,
            rt = {},
            nt = function(n) {
                var t = d[n];
                o(d, n, n == "delete" ?
                function(n) {
                    return b && !u(n) ? !1 : t.call(this, n === 0 ? 0 : n)
                }: n == "has" ?
                function(n) {
                    return b && !u(n) ? !1 : t.call(this, n === 0 ? 0 : n)
                }: n == "get" ?
                function(n) {
                    return b && !u(n) ? undefined: t.call(this, n === 0 ? 0 : n)
                }: n == "add" ?
                function(n) {
                    return t.call(this, n === 0 ? 0 : n),
                    this
                }: function(n, i) {
                    return t.call(this, n === 0 ? 0 : n, i),
                    this
                })
            };
            if (typeof k == "function" && (b || d.forEach && !f(function() { (new k).entries().next()
            }))) {
                var it = new k,
                ft = it[g](b ? {}: -0, 1) != it,
                et = f(function() {
                    it.has(1)
                }),
                ot = a(function(n) {
                    new k(n)
                }),
                ut = !b && f(function() {
                    for (var t = new k,
                    n = 5; n--;) t[g](n, n);
                    return ! t.has( - 0)
                });
                ot || (k = t(function(t, i) {
                    l(t, k, n);
                    var r = y(new tt, t, k);
                    return i != undefined && c(i, w, r[g], r),
                    r
                }), k.prototype = d, d.constructor = k); (et || ut) && (nt("delete"), nt("has"), w && nt("get")); (ut || ft) && nt(g);
                b && d.clear && delete d.clear
            } else k = p.getConstructor(t, n, w, g),
            s(k.prototype, i),
            h.NEED = !0;
            return v(k, n),
            rt[n] = k,
            r(r.G + r.W + r.F * (k != tt), rt),
            b || p.setStrong(k, n, w),
            k
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(1),
        r = i(105).set;
        n.exports = function(n, t, i) {
            var f = t.constructor,
            e;
            return f !== i && typeof f == "function" && (e = f.prototype) !== i.prototype && u(e) && r && r(n, e),
            n
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(1),
        f = i(6),
        r = function(n, t) {
            if (f(n), !u(t) && t !== null) throw TypeError(t + ": can't set as prototype!");
        };
        n.exports = {
            set: Object.setPrototypeOf || ("__proto__" in {} ?
            function(n, t, u) {
                try {
                    u = i(10)(Function.call, i(106).f(Object.prototype, "__proto__").set, 2);
                    u(n, []);
                    t = !(n instanceof Array)
                } catch(f) {
                    t = !0
                }
                return function(n, i) {
                    return r(n, i),
                    t ? n.__proto__ = i: u(n, i),
                    n
                }
            } ({},
            !1) : undefined),
            check: r
        }
    },
    function(n, t, i) {
        "use strict";
        var u = i(31),
        f = i(25),
        e = i(17),
        o = i(40),
        s = i(7),
        h = i(39),
        r = Object.getOwnPropertyDescriptor;
        t.f = i(12) ? r: function(n, t) {
            if (n = e(n), t = o(t, !0), h) try {
                return r(n, t)
            } catch(i) {}
            if (s(n, t)) return f(!u.f.call(n, t), n[t])
        }
    },
    function(n, t, i) {
        "use strict";
        i(108)("WeakMap")
    },
    function(n, t, i) {
        "use strict";
        var r = i(4);
        n.exports = function(n) {
            r(r.S, n, {
                of: function() {
                    for (var n = arguments.length,
                    t = Array(n); n--;) t[n] = arguments[n];
                    return new this(t)
                }
            })
        }
    },
    function(n, t, i) {
        "use strict";
        i(110)("WeakMap")
    },
    function(n, t, i) {
        "use strict";
        var r = i(4),
        u = i(16),
        e = i(10),
        f = i(23);
        n.exports = function(n) {
            r(r.S, n, {
                from: function(n) {
                    var i = arguments[1],
                    r,
                    t,
                    o,
                    s;
                    return (u(this), r = i !== undefined, r && u(i), n == undefined) ? new this: (t = [], r ? (o = 0, s = e(i, arguments[2], 2), f(n, !1,
                    function(n) {
                        t.push(s(n, o++))
                    })) : f(n, !1, t.push, t), new this(t))
                }
            })
        }
    },
    function(n, t, i) {
        "use strict";
        var r = !1;
        if (typeof ReadableStream != "undefined") try {
            new ReadableStream({
                start: function(n) {
                    n.close()
                }
            });
            r = !0
        } catch(f) {}
        t.ReadableStream = r ? ReadableStream: i(112).ReadableStream
    },
    function(n, t) {
        "use strict";
        var i = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ?
        function(n) {
            return typeof n
        }: function(n) {
            return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n
        }; (function(n, t) {
            for (var i in t) n[i] = t[i]
        })(t,
        function(n) {
            function t(r) {
                if (i[r]) return i[r].exports;
                var u = i[r] = {
                    i: r,
                    l: !1,
                    exports: {}
                };
                return n[r].call(u.exports, u, u.exports, t),
                u.l = !0,
                u.exports
            }
            var i = {};
            return t.m = n,
            t.c = i,
            t.i = function(n) {
                return n
            },
            t.d = function(n, i, r) {
                t.o(n, i) || Object.defineProperty(n, i, {
                    configurable: !1,
                    enumerable: !0,
                    get: r
                })
            },
            t.n = function(n) {
                var i = n && n.__esModule ?
                function() {
                    return n["default"]
                }: function() {
                    return n
                };
                return t.d(i, "a", i),
                i
            },
            t.o = function(n, t) {
                return Object.prototype.hasOwnProperty.call(n, t)
            },
            t.p = "",
            t(t.s = 7)
        } ([function(n, t, r) {
            function f(n) {
                return typeof n == "string" || (typeof n == "undefined" ? "undefined": e(n)) === "symbol"
            }
            function o(n, t, i) {
                if (typeof n != "function") throw new TypeError("Argument is not a function");
                return Function.prototype.apply.call(n, t, i)
            }
            var e = typeof Symbol == "function" && i(Symbol.iterator) === "symbol" ?
            function(n) {
                return typeof n == "undefined" ? "undefined": i(n)
            }: function(n) {
                return n && typeof Symbol == "function" && n.constructor === Symbol && n !== Symbol.prototype ? "symbol": typeof n == "undefined" ? "undefined": i(n)
            },
            s = r(1),
            u = s.assert;
            t.typeIsObject = function(n) {
                return (typeof n == "undefined" ? "undefined": e(n)) === "object" && n !== null || typeof n == "function"
            };
            t.createDataProperty = function(n, i, r) {
                u(t.typeIsObject(n));
                Object.defineProperty(n, i, {
                    value: r,
                    writable: !0,
                    enumerable: !0,
                    configurable: !0
                })
            };
            t.createArrayFromList = function(n) {
                return n.slice()
            };
            t.ArrayBufferCopy = function(n, t, i, r, u) {
                new Uint8Array(n).set(new Uint8Array(i, r, u), t)
            };
            t.CreateIterResultObject = function(n, t) {
                u(typeof t == "boolean");
                var i = {};
                return Object.defineProperty(i, "value", {
                    value: n,
                    enumerable: !0,
                    writable: !0,
                    configurable: !0
                }),
                Object.defineProperty(i, "done", {
                    value: t,
                    enumerable: !0,
                    writable: !0,
                    configurable: !0
                }),
                i
            };
            t.IsFiniteNonNegativeNumber = function(n) {
                return Number.isNaN(n) ? !1 : n === Infinity ? !1 : n < 0 ? !1 : !0
            };
            t.InvokeOrNoop = function(n, t, i) {
                u(n !== undefined);
                u(f(t));
                u(Array.isArray(i));
                var r = n[t];
                return r === undefined ? undefined: o(r, n, i)
            };
            t.PromiseInvokeOrNoop = function(n, i, r) {
                u(n !== undefined);
                u(f(i));
                u(Array.isArray(r));
                try {
                    return Promise.resolve(t.InvokeOrNoop(n, i, r))
                } catch(e) {
                    return Promise.reject(e)
                }
            };
            t.PromiseInvokeOrPerformFallback = function(n, t, i, r, e) {
                u(n !== undefined);
                u(f(t));
                u(Array.isArray(i));
                u(Array.isArray(e));
                var s = void 0;
                try {
                    s = n[t]
                } catch(h) {
                    return Promise.reject(h)
                }
                if (s === undefined) return r.apply(null, e);
                try {
                    return Promise.resolve(o(s, n, i))
                } catch(c) {
                    return Promise.reject(c)
                }
            };
            t.TransferArrayBuffer = function(n) {
                return n.slice()
            };
            t.ValidateAndNormalizeHighWaterMark = function(n) {
                if (n = Number(n), Number.isNaN(n) || n < 0) throw new RangeError("highWaterMark property of a queuing strategy must be non-negative and non-NaN");
                return n
            };
            t.ValidateAndNormalizeQueuingStrategy = function(n, i) {
                if (n !== undefined && typeof n != "function") throw new TypeError("size property of a queuing strategy must be a function");
                return i = t.ValidateAndNormalizeHighWaterMark(i),
                {
                    size: n,
                    highWaterMark: i
                }
            }
        },
        function(n) {
            function i(n) {
                n && n.constructor === t && setTimeout(function() {
                    throw n;
                },
                0)
            }
            function t(n) {
                this.name = "AssertionError";
                this.message = n || "";
                this.stack = (new Error).stack
            }
            function r(n, i) {
                if (!n) throw new t(i);
            }
            t.prototype = Object.create(Error.prototype);
            t.prototype.constructor = t;
            n.exports = {
                rethrowAssertionErrorRejection: i,
                AssertionError: t,
                assert: r
            }
        },
        function(n, t, i) {
            function p(n, t) {
                if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
            }
            function ct(n) {
                return new at(n)
            }
            function f(n) {
                return b(n) ? Object.prototype.hasOwnProperty.call(n, "_writableStreamController") ? !0 : !1 : !1
            }
            function s(n) {
                return (r(f(n) === !0, "IsWritableStreamLocked should only be used on known writable streams"), n._writer === undefined) ? !1 : !0
            }
            function d(n, t) {
                var i = n._state,
                f, u, e;
                return i === "closed" ? Promise.resolve(undefined) : i === "errored" ? Promise.reject(n._storedError) : (f = new TypeError("Requested to abort"), n._pendingAbortRequest !== undefined) ? Promise.reject(f) : (r(i === "writable" || i === "erroring", "state must be writable or erroring"), u = !1, i === "erroring" && (u = !0, t = undefined), e = new Promise(function(i, r) {
                    n._pendingAbortRequest = {
                        _resolve: i,
                        _reject: r,
                        _reason: t,
                        _wasAlreadyErroring: u
                    }
                }), u === !1 && nt(n, f), e)
            }
            function si(n) {
                r(s(n) === !0);
                r(n._state === "writable");
                return new Promise(function(t, i) {
                    var r = {
                        _resolve: t,
                        _reject: i
                    };
                    n._writeRequests.push(r)
                })
            }
            function g(n, t) {
                var i = n._state;
                if (i === "writable") {
                    nt(n, t);
                    return
                }
                r(i === "erroring");
                tt(n)
            }
            function nt(n, t) {
                var i, u;
                r(n._storedError === undefined, "stream._storedError === undefined");
                r(n._state === "writable", "state must be writable");
                i = n._writableStreamController;
                r(i !== undefined, "controller must not be undefined");
                n._state = "erroring";
                n._storedError = t;
                u = n._writer;
                u !== undefined && yt(u, t);
                lt(n) === !1 && i._started === !0 && tt(n)
            }
            function tt(n) {
                var u, i, f, t, e;
                for (r(n._state === "erroring", "stream._state === erroring"), r(lt(n) === !1, "WritableStreamHasOperationMarkedInFlight(stream) === false"), n._state = "errored", n._writableStreamController.__errorSteps(), u = n._storedError, i = 0; i < n._writeRequests.length; i++) f = n._writeRequests[i],
                f._reject(u);
                if (n._writeRequests = [], n._pendingAbortRequest === undefined) {
                    a(n);
                    return
                }
                if (t = n._pendingAbortRequest, n._pendingAbortRequest = undefined, t._wasAlreadyErroring === !0) {
                    t._reject(u);
                    a(n);
                    return
                }
                e = n._writableStreamController.__abortSteps(t._reason);
                e.then(function() {
                    t._resolve();
                    a(n)
                },
                function(i) {
                    t._reject(i);
                    a(n)
                })
            }
            function hi(n) {
                r(n._inFlightWriteRequest !== undefined);
                n._inFlightWriteRequest._resolve(undefined);
                n._inFlightWriteRequest = undefined
            }
            function ci(n, t) {
                r(n._inFlightWriteRequest !== undefined);
                n._inFlightWriteRequest._reject(t);
                n._inFlightWriteRequest = undefined;
                r(n._state === "writable" || n._state === "erroring");
                g(n, t)
            }
            function li(n) {
                var t, i;
                r(n._inFlightCloseRequest !== undefined);
                n._inFlightCloseRequest._resolve(undefined);
                n._inFlightCloseRequest = undefined;
                t = n._state;
                r(t === "writable" || t === "erroring");
                t === "erroring" && (n._storedError = undefined, n._pendingAbortRequest !== undefined && (n._pendingAbortRequest._resolve(), n._pendingAbortRequest = undefined));
                n._state = "closed";
                i = n._writer;
                i !== undefined && or(i);
                r(n._pendingAbortRequest === undefined, "stream._pendingAbortRequest === undefined");
                r(n._storedError === undefined, "stream._storedError === undefined")
            }
            function ai(n, t) {
                r(n._inFlightCloseRequest !== undefined);
                n._inFlightCloseRequest._reject(t);
                n._inFlightCloseRequest = undefined;
                r(n._state === "writable" || n._state === "erroring");
                n._pendingAbortRequest !== undefined && (n._pendingAbortRequest._reject(t), n._pendingAbortRequest = undefined);
                g(n, t)
            }
            function u(n) {
                return n._closeRequest === undefined && n._inFlightCloseRequest === undefined ? !1 : !0
            }
            function lt(n) {
                return n._inFlightWriteRequest === undefined && n._inFlightCloseRequest === undefined ? !1 : !0
            }
            function vi(n) {
                r(n._inFlightCloseRequest === undefined);
                r(n._closeRequest !== undefined);
                n._inFlightCloseRequest = n._closeRequest;
                n._closeRequest = undefined
            }
            function yi(n) {
                r(n._inFlightWriteRequest === undefined, "there must be no pending write request");
                r(n._writeRequests.length !== 0, "writeRequests must not be empty");
                n._inFlightWriteRequest = n._writeRequests.shift()
            }
            function a(n) {
                r(n._state === "errored", '_stream_.[[state]] is `"errored"`');
                n._closeRequest !== undefined && (r(n._inFlightCloseRequest === undefined), n._closeRequest._reject(n._storedError), n._closeRequest = undefined);
                var t = n._writer;
                t !== undefined && (ni(t, n._storedError), t._closedPromise.
                catch(function() {}))
            }
            function it(n, t) {
                r(n._state === "writable");
                r(u(n) === !1);
                var i = n._writer;
                i !== undefined && t !== n._backpressure && (t === !0 ? cr(i) : (r(t === !1), ri(i)));
                n._backpressure = t
            }
            function e(n) {
                return b(n) ? Object.prototype.hasOwnProperty.call(n, "_ownerWritableStream") ? !0 : !1 : !1
            }
            function pi(n, t) {
                var i = n._ownerWritableStream;
                return r(i !== undefined),
                d(i, t)
            }
            function vt(n) {
                var i = n._ownerWritableStream,
                t, f;
                return (r(i !== undefined), t = i._state, t === "closed" || t === "errored") ? Promise.reject(new TypeError("The stream (in " + t + " state) is not in the writable state and cannot be closed")) : (r(t === "writable" || t === "erroring"), r(u(i) === !1), f = new Promise(function(n, t) {
                    var r = {
                        _resolve: n,
                        _reject: t
                    };
                    i._closeRequest = r
                }), i._backpressure === !0 && t === "writable" && ri(n), di(i._writableStreamController), f)
            }
            function wi(n) {
                var i = n._ownerWritableStream,
                t;
                return (r(i !== undefined), t = i._state, u(i) === !0 || t === "closed") ? Promise.resolve() : t === "errored" ? Promise.reject(i._storedError) : (r(t === "writable" || t === "erroring"), vt(n))
            }
            function bi(n, t) {
                n._closedPromiseState === "pending" ? ni(n, t) : er(n, t);
                n._closedPromise.
                catch(function() {})
            }
            function yt(n, t) {
                n._readyPromiseState === "pending" ? hr(n, t) : lr(n, t);
                n._readyPromise.
                catch(function() {})
            }
            function ki(n) {
                var i = n._ownerWritableStream,
                t = i._state;
                return t === "errored" || t === "erroring" ? null: t === "closed" ? 0 : kt(i._writableStreamController)
            }
            function pt(n) {
                var t = n._ownerWritableStream,
                i;
                r(t !== undefined);
                r(t._writer === n);
                i = new TypeError("Writer was released and can no longer be used to monitor the stream's closedness");
                yt(n, i);
                bi(n, i);
                t._writer = undefined;
                n._ownerWritableStream = undefined
            }
            function wt(n, t) {
                var i = n._ownerWritableStream,
                e, o, f, s;
                return (r(i !== undefined), e = i._writableStreamController, o = gi(e, t), i !== n._ownerWritableStream) ? Promise.reject(h("write to")) : (f = i._state, f === "errored") ? Promise.reject(i._storedError) : u(i) === !0 || f === "closed" ? Promise.reject(new TypeError("The stream is closing or closed and cannot be written to")) : f === "erroring" ? Promise.reject(i._storedError) : (r(f === "writable"), s = si(i), nr(e, t, o), s)
            }
            function di(n) {
                st(n, "close", 0);
                v(n)
            }
            function gi(n, t) {
                var i = n._strategySize;
                if (i === undefined) return 1;
                try {
                    return i(t)
                } catch(r) {
                    return dt(n, r),
                    1
                }
            }
            function kt(n) {
                return n._strategyHWM - n._queueTotalSize
            }
            function nr(n, t, i) {
                var e = {
                    chunk: t
                },
                r,
                f;
                try {
                    st(n, e, i)
                } catch(o) {
                    dt(n, o);
                    return
                }
                r = n._controlledWritableStream;
                u(r) === !1 && r._state === "writable" && (f = rt(n), it(r, f));
                v(n)
            }
            function tr(n) {
                return b(n) ? Object.prototype.hasOwnProperty.call(n, "_underlyingSink") ? !0 : !1 : !1
            }
            function v(n) {
                var i = n._controlledWritableStream,
                t, r;
                if (n._started !== !1 && i._inFlightWriteRequest === undefined && (t = i._state, t !== "closed" && t !== "errored")) {
                    if (t === "erroring") {
                        tt(i);
                        return
                    }
                    n._queue.length !== 0 && (r = ei(n), r === "close" ? ir(n) : rr(n, r.chunk))
                }
            }
            function dt(n, t) {
                n._controlledWritableStream._state === "writable" && ut(n, t)
            }
            function ir(n) {
                var t = n._controlledWritableStream,
                i;
                vi(t);
                ot(n);
                r(n._queue.length === 0, "queue must be empty once the final write record is dequeued");
                i = w(n._underlyingSink, "close", []);
                i.then(function() {
                    li(t)
                },
                function(n) {
                    ai(t, n)
                }).
                catch(k)
            }
            function rr(n, t) {
                var i = n._controlledWritableStream,
                f;
                yi(i);
                f = w(n._underlyingSink, "write", [t, n]);
                f.then(function() {
                    var t, f;
                    hi(i);
                    t = i._state;
                    r(t === "writable" || t === "erroring");
                    ot(n);
                    u(i) === !1 && t === "writable" && (f = rt(n), it(i, f));
                    v(n)
                },
                function(n) {
                    ci(i, n)
                }).
                catch(k)
            }
            function rt(n) {
                var t = kt(n);
                return t <= 0
            }
            function ut(n, t) {
                var i = n._controlledWritableStream;
                r(i._state === "writable");
                nt(i, t)
            }
            function ft(n) {
                return new TypeError("WritableStream.prototype." + n + " can only be used on a WritableStream")
            }
            function o(n) {
                return new TypeError("WritableStreamDefaultWriter.prototype." + n + " can only be used on a WritableStreamDefaultWriter")
            }
            function h(n) {
                return new TypeError("Cannot " + n + " a stream using a released writer")
            }
            function gt(n) {
                n._closedPromise = new Promise(function(t, i) {
                    n._closedPromise_resolve = t;
                    n._closedPromise_reject = i;
                    n._closedPromiseState = "pending"
                })
            }
            function ur(n, t) {
                n._closedPromise = Promise.reject(t);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined;
                n._closedPromiseState = "rejected"
            }
            function fr(n) {
                n._closedPromise = Promise.resolve(undefined);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined;
                n._closedPromiseState = "resolved"
            }
            function ni(n, t) {
                r(n._closedPromise_resolve !== undefined, "writer._closedPromise_resolve !== undefined");
                r(n._closedPromise_reject !== undefined, "writer._closedPromise_reject !== undefined");
                r(n._closedPromiseState === "pending", "writer._closedPromiseState is pending");
                n._closedPromise_reject(t);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined;
                n._closedPromiseState = "rejected"
            }
            function er(n, t) {
                r(n._closedPromise_resolve === undefined, "writer._closedPromise_resolve === undefined");
                r(n._closedPromise_reject === undefined, "writer._closedPromise_reject === undefined");
                r(n._closedPromiseState !== "pending", "writer._closedPromiseState is not pending");
                n._closedPromise = Promise.reject(t);
                n._closedPromiseState = "rejected"
            }
            function or(n) {
                r(n._closedPromise_resolve !== undefined, "writer._closedPromise_resolve !== undefined");
                r(n._closedPromise_reject !== undefined, "writer._closedPromise_reject !== undefined");
                r(n._closedPromiseState === "pending", "writer._closedPromiseState is pending");
                n._closedPromise_resolve(undefined);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined;
                n._closedPromiseState = "resolved"
            }
            function sr(n) {
                n._readyPromise = new Promise(function(t, i) {
                    n._readyPromise_resolve = t;
                    n._readyPromise_reject = i
                });
                n._readyPromiseState = "pending"
            }
            function ti(n, t) {
                n._readyPromise = Promise.reject(t);
                n._readyPromise_resolve = undefined;
                n._readyPromise_reject = undefined;
                n._readyPromiseState = "rejected"
            }
            function ii(n) {
                n._readyPromise = Promise.resolve(undefined);
                n._readyPromise_resolve = undefined;
                n._readyPromise_reject = undefined;
                n._readyPromiseState = "fulfilled"
            }
            function hr(n, t) {
                r(n._readyPromise_resolve !== undefined, "writer._readyPromise_resolve !== undefined");
                r(n._readyPromise_reject !== undefined, "writer._readyPromise_reject !== undefined");
                n._readyPromise_reject(t);
                n._readyPromise_resolve = undefined;
                n._readyPromise_reject = undefined;
                n._readyPromiseState = "rejected"
            }
            function cr(n) {
                r(n._readyPromise_resolve === undefined, "writer._readyPromise_resolve === undefined");
                r(n._readyPromise_reject === undefined, "writer._readyPromise_reject === undefined");
                n._readyPromise = new Promise(function(t, i) {
                    n._readyPromise_resolve = t;
                    n._readyPromise_reject = i
                });
                n._readyPromiseState = "pending"
            }
            function lr(n, t) {
                r(n._readyPromise_resolve === undefined, "writer._readyPromise_resolve === undefined");
                r(n._readyPromise_reject === undefined, "writer._readyPromise_reject === undefined");
                n._readyPromise = Promise.reject(t);
                n._readyPromiseState = "rejected"
            }
            function ri(n) {
                r(n._readyPromise_resolve !== undefined, "writer._readyPromise_resolve !== undefined");
                r(n._readyPromise_reject !== undefined, "writer._readyPromise_reject !== undefined");
                n._readyPromise_resolve(undefined);
                n._readyPromise_resolve = undefined;
                n._readyPromise_reject = undefined;
                n._readyPromiseState = "fulfilled"
            }
            var y = function() {
                function n(n, t) {
                    for (var i, r = 0; r < t.length; r++) i = t[r],
                    i.enumerable = i.enumerable || !1,
                    i.configurable = !0,
                    "value" in i && (i.writable = !0),
                    Object.defineProperty(n, i.key, i)
                }
                return function(t, i, r) {
                    return i && n(t.prototype, i),
                    r && n(t, r),
                    t
                }
            } (),
            c = i(0),
            ui = c.InvokeOrNoop,
            w = c.PromiseInvokeOrNoop,
            fi = c.ValidateAndNormalizeQueuingStrategy,
            b = c.typeIsObject,
            et = i(1),
            r = et.assert,
            k = et.rethrowAssertionErrorRejection,
            l = i(3),
            ot = l.DequeueValue,
            st = l.EnqueueValueWithSize,
            ei = l.PeekQueueValue,
            ht = l.ResetQueue,
            oi = function() {
                function n() {
                    var t = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                    i = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
                    f = i.size,
                    r = i.highWaterMark,
                    e = r === undefined ? 1 : r,
                    u;
                    if (p(this, n), this._state = "writable", this._storedError = undefined, this._writer = undefined, this._writableStreamController = undefined, this._writeRequests = [], this._inFlightWriteRequest = undefined, this._closeRequest = undefined, this._inFlightCloseRequest = undefined, this._pendingAbortRequest = undefined, this._backpressure = !1, u = t.type, u !== undefined) throw new RangeError("Invalid type is specified");
                    this._writableStreamController = new bt(this, t, f, e);
                    this._writableStreamController.__startSteps()
                }
                return y(n, [{
                    key: "abort",
                    value: function(n) {
                        return f(this) === !1 ? Promise.reject(ft("abort")) : s(this) === !0 ? Promise.reject(new TypeError("Cannot abort a stream that already has a writer")) : d(this, n)
                    }
                },
                {
                    key: "getWriter",
                    value: function() {
                        if (f(this) === !1) throw ft("getWriter");
                        return ct(this)
                    }
                },
                {
                    key: "locked",
                    get: function() {
                        if (f(this) === !1) throw ft("locked");
                        return s(this)
                    }
                }]),
                n
            } (),
            at,
            bt;
            n.exports = {
                AcquireWritableStreamDefaultWriter: ct,
                IsWritableStream: f,
                IsWritableStreamLocked: s,
                WritableStream: oi,
                WritableStreamAbort: d,
                WritableStreamDefaultControllerError: ut,
                WritableStreamDefaultWriterCloseWithErrorPropagation: wi,
                WritableStreamDefaultWriterRelease: pt,
                WritableStreamDefaultWriterWrite: wt,
                WritableStreamCloseQueuedOrInFlight: u
            };
            at = function() {
                function n(t) {
                    var i, e;
                    if (p(this, n), f(t) === !1) throw new TypeError("WritableStreamDefaultWriter can only be constructed with a WritableStream instance");
                    if (s(t) === !0) throw new TypeError("This stream has already been locked for exclusive writing by another writer");
                    this._ownerWritableStream = t;
                    t._writer = this;
                    i = t._state;
                    i === "writable" ? (u(t) === !1 && t._backpressure === !0 ? sr(this) : ii(this), gt(this)) : i === "erroring" ? (ti(this, t._storedError), this._readyPromise.
                    catch(function() {}), gt(this)) : i === "closed" ? (ii(this), fr(this)) : (r(i === "errored", "state must be errored"), e = t._storedError, ti(this, e), this._readyPromise.
                    catch(function() {}), ur(this, e), this._closedPromise.
                    catch(function() {}))
                }
                return y(n, [{
                    key: "abort",
                    value: function(n) {
                        return e(this) === !1 ? Promise.reject(o("abort")) : this._ownerWritableStream === undefined ? Promise.reject(h("abort")) : pi(this, n)
                    }
                },
                {
                    key: "close",
                    value: function() {
                        if (e(this) === !1) return Promise.reject(o("close"));
                        var n = this._ownerWritableStream;
                        return n === undefined ? Promise.reject(h("close")) : u(n) === !0 ? Promise.reject(new TypeError("cannot close an already-closing stream")) : vt(this)
                    }
                },
                {
                    key: "releaseLock",
                    value: function() {
                        if (e(this) === !1) throw o("releaseLock");
                        var n = this._ownerWritableStream;
                        n !== undefined && (r(n._writer !== undefined), pt(this))
                    }
                },
                {
                    key: "write",
                    value: function(n) {
                        return e(this) === !1 ? Promise.reject(o("write")) : this._ownerWritableStream === undefined ? Promise.reject(h("write to")) : wt(this, n)
                    }
                },
                {
                    key: "closed",
                    get: function() {
                        return e(this) === !1 ? Promise.reject(o("closed")) : this._closedPromise
                    }
                },
                {
                    key: "desiredSize",
                    get: function() {
                        if (e(this) === !1) throw o("desiredSize");
                        if (this._ownerWritableStream === undefined) throw h("desiredSize");
                        return ki(this)
                    }
                },
                {
                    key: "ready",
                    get: function() {
                        return e(this) === !1 ? Promise.reject(o("ready")) : this._readyPromise
                    }
                }]),
                n
            } ();
            bt = function() {
                function n(t, i, r, u) {
                    var e, o;
                    if (p(this, n), f(t) === !1) throw new TypeError("WritableStreamDefaultController can only be constructed with a WritableStream instance");
                    if (t._writableStreamController !== undefined) throw new TypeError("WritableStreamDefaultController instances can only be created by the WritableStream constructor");
                    this._controlledWritableStream = t;
                    this._underlyingSink = i;
                    this._queue = undefined;
                    this._queueTotalSize = undefined;
                    ht(this);
                    this._started = !1;
                    e = fi(r, u);
                    this._strategySize = e.size;
                    this._strategyHWM = e.highWaterMark;
                    o = rt(this);
                    it(t, o)
                }
                return y(n, [{
                    key: "error",
                    value: function(n) {
                        if (tr(this) === !1) throw new TypeError("WritableStreamDefaultController.prototype.error can only be used on a WritableStreamDefaultController");
                        var t = this._controlledWritableStream._state;
                        t === "writable" && ut(this, n)
                    }
                },
                {
                    key: "__abortSteps",
                    value: function(n) {
                        return w(this._underlyingSink, "abort", [n])
                    }
                },
                {
                    key: "__errorSteps",
                    value: function() {
                        ht(this)
                    }
                },
                {
                    key: "__startSteps",
                    value: function() {
                        var t = this,
                        i = ui(this._underlyingSink, "start", [this]),
                        n = this._controlledWritableStream;
                        Promise.resolve(i).then(function() {
                            r(n._state === "writable" || n._state === "erroring");
                            t._started = !0;
                            v(t)
                        },
                        function(i) {
                            r(n._state === "writable" || n._state === "erroring");
                            t._started = !0;
                            g(n, i)
                        }).
                        catch(k)
                    }
                }]),
                n
            } ()
        },
        function(n, t, i) {
            var u = i(0),
            f = u.IsFiniteNonNegativeNumber,
            e = i(1),
            r = e.assert;
            t.DequeueValue = function(n) {
                r("_queue" in n && "_queueTotalSize" in n, "Spec-level failure: DequeueValue should only be used on containers with [[queue]] and [[queueTotalSize]].");
                r(n._queue.length > 0, "Spec-level failure: should never dequeue from an empty queue.");
                var t = n._queue.shift();
                return n._queueTotalSize -= t.size,
                n._queueTotalSize < 0 && (n._queueTotalSize = 0),
                t.value
            };
            t.EnqueueValueWithSize = function(n, t, i) {
                if (r("_queue" in n && "_queueTotalSize" in n, "Spec-level failure: EnqueueValueWithSize should only be used on containers with [[queue]] and [[queueTotalSize]]."), i = Number(i), !f(i)) throw new RangeError("Size must be a finite, non-NaN, non-negative number.");
                n._queue.push({
                    value: t,
                    size: i
                });
                n._queueTotalSize += i
            };
            t.PeekQueueValue = function(n) {
                r("_queue" in n && "_queueTotalSize" in n, "Spec-level failure: PeekQueueValue should only be used on containers with [[queue]] and [[queueTotalSize]].");
                r(n._queue.length > 0, "Spec-level failure: should never peek at an empty queue.");
                var t = n._queue[0];
                return t.value
            };
            t.ResetQueue = function(n) {
                r("_queue" in n && "_queueTotalSize" in n, "Spec-level failure: ResetQueue should only be used on containers with [[queue]] and [[queueTotalSize]].");
                n._queue = [];
                n._queueTotalSize = 0
            }
        },
        function(n, t, i) {
            function y(n, t) {
                if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
            }
            function eu(n) {
                return new ki(n)
            }
            function ti(n) {
                return new bi(n)
            }
            function u(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_readableStreamController") ? !0 : !1 : !1
            }
            function ou(n) {
                return r(u(n) === !0, "IsReadableStreamDisturbed should only be used on known readable streams"),
                n._disturbed
            }
            function h(n) {
                return (r(u(n) === !0, "IsReadableStreamLocked should only be used on known readable streams"), n._reader === undefined) ? !1 : !0
            }
            function su(n, t) {
                var c, f, i, e, o, s, l, h, a;
                return r(u(n) === !0),
                r(typeof t == "boolean"),
                c = ti(n),
                f = {
                    closedOrErrored: !1,
                    canceled1: !1,
                    canceled2: !1,
                    reason1: undefined,
                    reason2: undefined
                },
                f.promise = new Promise(function(n) {
                    f._resolve = n
                }),
                i = hu(),
                i._reader = c,
                i._teeState = f,
                i._cloneForBranch2 = t,
                e = cu(),
                e._stream = n,
                e._teeState = f,
                o = lu(),
                o._stream = n,
                o._teeState = f,
                s = Object.create(Object.prototype),
                ot(s, "pull", i),
                ot(s, "cancel", e),
                l = new ni(s),
                h = Object.create(Object.prototype),
                ot(h, "pull", i),
                ot(h, "cancel", o),
                a = new ni(h),
                i._branch1 = l._readableStreamController,
                i._branch2 = a._readableStreamController,
                c._closedPromise.
                catch(function(n) {
                    f.closedOrErrored !== !0 && (tt(i._branch1, n), tt(i._branch2, n), f.closedOrErrored = !0)
                }),
                [l, a]
            }
            function hu() {
                function n() {
                    var f = n._reader,
                    i = n._branch1,
                    u = n._branch2,
                    t = n._teeState;
                    return ei(f).then(function(n) {
                        var f, e, o, s; (r(l(n)), f = n.value, e = n.done, r(typeof e == "boolean"), e === !0 && t.closedOrErrored === !1 && (t.canceled1 === !1 && ht(i), t.canceled2 === !1 && ht(u), t.closedOrErrored = !0), t.closedOrErrored !== !0) && (o = f, s = f, t.canceled1 === !1 && ct(i, o), t.canceled2 === !1 && ct(u, s))
                    })
                }
                return n
            }
            function cu() {
                function n(t) {
                    var f = n._stream,
                    i = n._teeState,
                    r, u;
                    return i.canceled1 = !0,
                    i.reason1 = t,
                    i.canceled2 === !0 && (r = kt([i.reason1, i.reason2]), u = w(f, r), i._resolve(u)),
                    i.promise
                }
                return n
            }
            function lu() {
                function n(t) {
                    var f = n._stream,
                    i = n._teeState,
                    r, u;
                    return i.canceled2 = !0,
                    i.reason2 = t,
                    i.canceled1 === !0 && (r = kt([i.reason1, i.reason2]), u = w(f, r), i._resolve(u)),
                    i.promise
                }
                return n
            }
            function vi(n) {
                r(a(n._reader) === !0);
                r(n._state === "readable" || n._state === "closed");
                return new Promise(function(t, i) {
                    var r = {
                        _resolve: t,
                        _reject: i
                    };
                    n._reader._readIntoRequests.push(r)
                })
            }
            function yi(n) {
                r(c(n._reader) === !0);
                r(n._state === "readable");
                return new Promise(function(t, i) {
                    var r = {
                        _resolve: t,
                        _reject: i
                    };
                    n._reader._readRequests.push(r)
                })
            }
            function w(n, t) {
                if (n._disturbed = !0, n._state === "closed") return Promise.resolve(undefined);
                if (n._state === "errored") return Promise.reject(n._storedError);
                d(n);
                var i = n._readableStreamController.__cancelSteps(t);
                return i.then(function() {
                    return undefined
                })
            }
            function d(n) {
                var t, i, u;
                if (r(n._state === "readable"), n._state = "closed", t = n._reader, t === undefined) return undefined;
                if (c(t) === !0) {
                    for (i = 0; i < t._readRequests.length; i++) u = t._readRequests[i]._resolve,
                    u(o(undefined, !0));
                    t._readRequests = []
                }
                return of(t),
                undefined
            }
            function pi(n, t) {
                var i, f, o, e, s;
                if (r(u(n) === !0, "stream must be ReadableStream"), r(n._state === "readable", "state must be readable"), n._state = "errored", n._storedError = t, i = n._reader, i === undefined) return undefined;
                if (c(i) === !0) {
                    for (f = 0; f < i._readRequests.length; f++) o = i._readRequests[f],
                    o._reject(t);
                    i._readRequests = []
                } else {
                    for (r(a(i), "reader must be ReadableStreamBYOBReader"), e = 0; e < i._readIntoRequests.length; e++) s = i._readIntoRequests[e],
                    s._reject(t);
                    i._readIntoRequests = []
                }
                ar(i, t);
                i._closedPromise.
                catch(function() {})
            }
            function au(n, t, i) {
                var u = n._reader,
                f;
                r(u._readIntoRequests.length > 0);
                f = u._readIntoRequests.shift();
                f._resolve(o(t, i))
            }
            function ii(n, t, i) {
                var u = n._reader,
                f;
                r(u._readRequests.length > 0);
                f = u._readRequests.shift();
                f._resolve(o(t, i))
            }
            function wi(n) {
                return n._reader._readIntoRequests.length
            }
            function g(n) {
                return n._reader._readRequests.length
            }
            function ri(n) {
                var t = n._reader;
                return t === undefined ? !1 : a(t) === !1 ? !1 : !0
            }
            function ui(n) {
                var t = n._reader;
                return t === undefined ? !1 : c(t) === !1 ? !1 : !0
            }
            function a(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_readIntoRequests") ? !0 : !1 : !1
            }
            function c(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_readRequests") ? !0 : !1 : !1
            }
            function di(n, t) {
                n._ownerReadableStream = t;
                t._reader = n;
                t._state === "readable" ? rf(n) : t._state === "closed" ? ff(n) : (r(t._state === "errored", "state must be errored"), uf(n, t._storedError), n._closedPromise.
                catch(function() {}))
            }
            function gi(n, t) {
                var i = n._ownerReadableStream;
                return r(i !== undefined),
                w(i, t)
            }
            function fi(n) {
                r(n._ownerReadableStream !== undefined);
                r(n._ownerReadableStream._reader === n);
                n._ownerReadableStream._state === "readable" ? ar(n, new TypeError("Reader was released and can no longer be used to monitor the stream's closedness")) : ef(n, new TypeError("Reader was released and can no longer be used to monitor the stream's closedness"));
                n._closedPromise.
                catch(function() {});
                n._ownerReadableStream._reader = undefined;
                n._ownerReadableStream = undefined
            }
            function vu(n, t) {
                var i = n._ownerReadableStream;
                return (r(i !== undefined), i._disturbed = !0, i._state === "errored") ? Promise.reject(i._storedError) : pu(i._readableStreamController, t)
            }
            function ei(n) {
                var t = n._ownerReadableStream;
                return (r(t !== undefined), t._disturbed = !0, t._state === "closed") ? Promise.resolve(o(undefined, !0)) : t._state === "errored" ? Promise.reject(t._storedError) : (r(t._state === "readable"), t._readableStreamController.__pullSteps())
            }
            function st(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_underlyingSource") ? !0 : !1 : !1
            }
            function nt(n) {
                var i = yu(n),
                t;
                return i === !1 ? undefined: n._pulling === !0 ? (n._pullAgain = !0, undefined) : (r(n._pullAgain === !1), n._pulling = !0, t = et(n._underlyingSource, "pull", [n]), t.then(function() {
                    return (n._pulling = !1, n._pullAgain === !0) ? (n._pullAgain = !1, nt(n)) : undefined
                },
                function(t) {
                    lt(n, t)
                }).
                catch(f), undefined)
            }
            function yu(n) {
                var t = n._controlledReadableStream,
                i;
                return t._state === "closed" || t._state === "errored" ? !1 : n._closeRequested === !0 ? !1 : n._started === !1 ? !1 : h(t) === !0 && g(t) > 0 ? !0 : (i = oi(n), i > 0) ? !0 : !1
            }
            function ht(n) {
                var t = n._controlledReadableStream;
                r(n._closeRequested === !1);
                r(t._state === "readable");
                n._closeRequested = !0;
                n._queue.length === 0 && d(t)
            }
            function ct(n, t) {
                var i = n._controlledReadableStream,
                u, f;
                if (r(n._closeRequested === !1), r(i._state === "readable"), h(i) === !0 && g(i) > 0) ii(i, t, !1);
                else {
                    if (u = 1, n._strategySize !== undefined) {
                        f = n._strategySize;
                        try {
                            u = f(t)
                        } catch(e) {
                            lt(n, e);
                            throw e;
                        }
                    }
                    try {
                        dr(n, t, u)
                    } catch(o) {
                        lt(n, o);
                        throw o;
                    }
                }
                return nt(n),
                undefined
            }
            function tt(n, t) {
                var i = n._controlledReadableStream;
                r(i._state === "readable");
                p(n);
                pi(i, t)
            }
            function lt(n, t) {
                n._controlledReadableStream._state === "readable" && tt(n, t)
            }
            function oi(n) {
                var i = n._controlledReadableStream,
                t = i._state;
                return t === "errored" ? null: t === "closed" ? 0 : n._strategyHWM - n._queueTotalSize
            }
            function b(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_underlyingByteSource") ? !0 : !1 : !1
            }
            function rr(n) {
                return l(n) ? Object.prototype.hasOwnProperty.call(n, "_associatedReadableByteStreamController") ? !0 : !1 : !1
            }
            function it(n) {
                var i = ku(n),
                t;
                return i === !1 ? undefined: n._pulling === !0 ? (n._pullAgain = !0, undefined) : (r(n._pullAgain === !1), n._pulling = !0, t = et(n._underlyingByteSource, "pull", [n]), t.then(function() {
                    n._pulling = !1;
                    n._pullAgain === !0 && (n._pullAgain = !1, it(n))
                },
                function(t) {
                    n._controlledReadableStream._state === "readable" && rt(n, t)
                }).
                catch(f), undefined)
            }
            function ur(n) {
                hi(n);
                n._pendingPullIntos = []
            }
            function si(n, t) {
                var i, u;
                r(n._state !== "errored", "state must not be errored");
                i = !1;
                n._state === "closed" && (r(t.bytesFilled === 0), i = !0);
                u = fr(t);
                t.readerType === "default" ? ii(n, u, i) : (r(t.readerType === "byob"), au(n, u, i))
            }
            function fr(n) {
                var t = n.bytesFilled,
                i = n.elementSize;
                return r(t <= n.byteLength),
                r(t % i == 0),
                new n.ctor(n.buffer, n.byteOffset, t / i)
            }
            function at(n, t, i, r) {
                n._queue.push({
                    buffer: t,
                    byteOffset: i,
                    byteLength: r
                });
                n._queueTotalSize += r
            }
            function er(n, t) {
                var s = t.elementSize,
                a = t.bytesFilled - t.bytesFilled % s,
                h = Math.min(n._queueTotalSize, t.byteLength - t.bytesFilled),
                c = t.bytesFilled + h,
                l = c - c % s,
                f = h,
                e = !1,
                o;
                for (l > a && (f = l - t.bytesFilled, e = !0), o = n._queue; f > 0;) {
                    var u = o[0],
                    i = Math.min(f, u.byteLength),
                    v = t.byteOffset + t.bytesFilled;
                    yr(t.buffer, v, u.buffer, u.byteOffset, i);
                    u.byteLength === i ? o.shift() : (u.byteOffset += i, u.byteLength -= i);
                    n._queueTotalSize -= i;
                    or(n, i, t);
                    f -= i
                }
                return e === !1 && (r(n._queueTotalSize === 0, "queue must be empty"), r(t.bytesFilled > 0), r(t.bytesFilled < t.elementSize)),
                e
            }
            function or(n, t, i) {
                r(n._pendingPullIntos.length === 0 || n._pendingPullIntos[0] === i);
                hi(n);
                i.bytesFilled += t
            }
            function sr(n) {
                r(n._controlledReadableStream._state === "readable");
                n._queueTotalSize === 0 && n._closeRequested === !0 ? d(n._controlledReadableStream) : it(n)
            }
            function hi(n) {
                n._byobRequest !== undefined && (n._byobRequest._associatedReadableByteStreamController = undefined, n._byobRequest._view = undefined, n._byobRequest = undefined)
            }
            function hr(n) {
                for (r(n._closeRequested === !1); n._pendingPullIntos.length > 0;) {
                    if (n._queueTotalSize === 0) return;
                    var t = n._pendingPullIntos[0];
                    er(n, t) === !0 && (ci(n), si(n._controlledReadableStream, t))
                }
            }
            function pu(n, t) {
                var r = n._controlledReadableStream,
                f = 1,
                e, i, s, h, u, c;
                if (t.constructor !== DataView && (f = t.constructor.BYTES_PER_ELEMENT), e = t.constructor, i = {
                    buffer: t.buffer,
                    byteOffset: t.byteOffset,
                    byteLength: t.byteLength,
                    bytesFilled: 0,
                    elementSize: f,
                    ctor: e,
                    readerType: "byob"
                },
                n._pendingPullIntos.length > 0) return i.buffer = k(i.buffer),
                n._pendingPullIntos.push(i),
                vi(r);
                if (r._state === "closed") return s = new t.constructor(i.buffer, i.byteOffset, 0),
                Promise.resolve(o(s, !0));
                if (n._queueTotalSize > 0) {
                    if (er(n, i) === !0) return h = fr(i),
                    sr(n),
                    Promise.resolve(o(h, !1));
                    if (n._closeRequested === !0) return u = new TypeError("Insufficient bytes to fill elements in the given buffer"),
                    rt(n, u),
                    Promise.reject(u)
                }
                return i.buffer = k(i.buffer),
                n._pendingPullIntos.push(i),
                c = vi(r),
                it(n),
                c
            }
            function wu(n, t) {
                var i, u;
                if (t.buffer = k(t.buffer), r(t.bytesFilled === 0, "bytesFilled must be 0"), i = n._controlledReadableStream, ri(i) === !0) while (wi(i) > 0) u = ci(n),
                si(i, u)
            }
            function bu(n, t, i) {
                var r, u, f;
                if (i.bytesFilled + t > i.byteLength) throw new RangeError("bytesWritten out of range"); (or(n, t, i), i.bytesFilled < i.elementSize) || (ci(n), r = i.bytesFilled % i.elementSize, r > 0 && (u = i.byteOffset + i.bytesFilled, f = i.buffer.slice(u - r, u), at(n, f, 0, f.byteLength)), i.buffer = k(i.buffer), i.bytesFilled -= r, si(n._controlledReadableStream, i), hr(n))
            }
            function cr(n, t) {
                var i = n._pendingPullIntos[0],
                u = n._controlledReadableStream;
                if (u._state === "closed") {
                    if (t !== 0) throw new TypeError("bytesWritten must be 0 when calling respond() on a closed stream");
                    wu(n, i)
                } else r(u._state === "readable"),
                bu(n, t, i)
            }
            function ci(n) {
                var t = n._pendingPullIntos.shift();
                return hi(n),
                t
            }
            function ku(n) {
                var t = n._controlledReadableStream;
                return t._state !== "readable" ? !1 : n._closeRequested === !0 ? !1 : n._started === !1 ? !1 : ui(t) === !0 && g(t) > 0 ? !0 : ri(t) === !0 && wi(t) > 0 ? !0 : lr(n) > 0 ? !0 : !1
            }
            function du(n) {
                var i = n._controlledReadableStream,
                u, t;
                if (r(n._closeRequested === !1), r(i._state === "readable"), n._queueTotalSize > 0) {
                    n._closeRequested = !0;
                    return
                }
                if (n._pendingPullIntos.length > 0 && (u = n._pendingPullIntos[0], u.bytesFilled > 0)) {
                    t = new TypeError("Insufficient bytes to fill elements in the given buffer");
                    rt(n, t);
                    throw t;
                }
                d(i)
            }
            function gu(n, t) {
                var i = n._controlledReadableStream,
                o;
                r(n._closeRequested === !1);
                r(i._state === "readable");
                var s = t.buffer,
                u = t.byteOffset,
                f = t.byteLength,
                e = k(s);
                ui(i) === !0 ? g(i) === 0 ? at(n, e, u, f) : (r(n._queue.length === 0), o = new Uint8Array(e, u, f), ii(i, o, !1)) : ri(i) === !0 ? (at(n, e, u, f), hr(n)) : (r(h(i) === !1, "stream must not be locked"), at(n, e, u, f))
            }
            function rt(n, t) {
                var i = n._controlledReadableStream;
                r(i._state === "readable");
                ur(n);
                p(n);
                pi(i, t)
            }
            function lr(n) {
                var i = n._controlledReadableStream,
                t = i._state;
                return t === "errored" ? null: t === "closed" ? 0 : n._strategyHWM - n._queueTotalSize
            }
            function nf(n, t) {
                if (t = Number(t), pr(t) === !1) throw new RangeError("bytesWritten must be a finite");
                r(n._pendingPullIntos.length > 0);
                cr(n, t)
            }
            function tf(n, t) {
                r(n._pendingPullIntos.length > 0);
                var i = n._pendingPullIntos[0];
                if (i.byteOffset + i.bytesFilled !== t.byteOffset) throw new RangeError("The region specified by view does not match byobRequest");
                if (i.byteLength !== t.byteLength) throw new RangeError("The buffer of view has different capacity than byobRequest");
                i.buffer = t.buffer;
                cr(n, t.byteLength)
            }
            function ut(n) {
                return new TypeError("ReadableStream.prototype." + n + " can only be used on a ReadableStream")
            }
            function vt(n) {
                return new TypeError("Cannot " + n + " a stream using a released reader")
            }
            function yt(n) {
                return new TypeError("ReadableStreamDefaultReader.prototype." + n + " can only be used on a ReadableStreamDefaultReader")
            }
            function rf(n) {
                n._closedPromise = new Promise(function(t, i) {
                    n._closedPromise_resolve = t;
                    n._closedPromise_reject = i
                })
            }
            function uf(n, t) {
                n._closedPromise = Promise.reject(t);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined
            }
            function ff(n) {
                n._closedPromise = Promise.resolve(undefined);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined
            }
            function ar(n, t) {
                r(n._closedPromise_resolve !== undefined);
                r(n._closedPromise_reject !== undefined);
                n._closedPromise_reject(t);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined
            }
            function ef(n, t) {
                r(n._closedPromise_resolve === undefined);
                r(n._closedPromise_reject === undefined);
                n._closedPromise = Promise.reject(t)
            }
            function of(n) {
                r(n._closedPromise_resolve !== undefined);
                r(n._closedPromise_reject !== undefined);
                n._closedPromise_resolve(undefined);
                n._closedPromise_resolve = undefined;
                n._closedPromise_reject = undefined
            }
            function pt(n) {
                return new TypeError("ReadableStreamBYOBReader.prototype." + n + " can only be used on a ReadableStreamBYOBReader")
            }
            function wt(n) {
                return new TypeError("ReadableStreamDefaultController.prototype." + n + " can only be used on a ReadableStreamDefaultController")
            }
            function vr(n) {
                return new TypeError("ReadableStreamBYOBRequest.prototype." + n + " can only be used on a ReadableStreamBYOBRequest")
            }
            function ft(n) {
                return new TypeError("ReadableByteStreamController.prototype." + n + " can only be used on a ReadableByteStreamController")
            }
            function sf(n) {
                try {
                    Promise.prototype.then.call(n, undefined,
                    function() {})
                } catch(t) {}
            }
            var v = function() {
                function n(n, t) {
                    for (var i, r = 0; r < t.length; r++) i = t[r],
                    i.enumerable = i.enumerable || !1,
                    i.configurable = !0,
                    "value" in i && (i.writable = !0),
                    Object.defineProperty(n, i.key, i)
                }
                return function(t, i, r) {
                    return i && n(t.prototype, i),
                    r && n(t, r),
                    t
                }
            } (),
            e = i(0),
            yr = e.ArrayBufferCopy,
            o = e.CreateIterResultObject,
            pr = e.IsFiniteNonNegativeNumber,
            li = e.InvokeOrNoop,
            et = e.PromiseInvokeOrNoop,
            k = e.TransferArrayBuffer,
            wr = e.ValidateAndNormalizeQueuingStrategy,
            br = e.ValidateAndNormalizeHighWaterMark,
            bt = i(0),
            kt = bt.createArrayFromList,
            ot = bt.createDataProperty,
            l = bt.typeIsObject,
            ai = i(1),
            r = ai.assert,
            f = ai.rethrowAssertionErrorRejection,
            dt = i(3),
            kr = dt.DequeueValue,
            dr = dt.EnqueueValueWithSize,
            p = dt.ResetQueue,
            s = i(2),
            gr = s.AcquireWritableStreamDefaultWriter,
            nu = s.IsWritableStream,
            tu = s.IsWritableStreamLocked,
            iu = s.WritableStreamAbort,
            ru = s.WritableStreamDefaultWriterCloseWithErrorPropagation,
            uu = s.WritableStreamDefaultWriterRelease,
            fu = s.WritableStreamDefaultWriterWrite,
            gt = s.WritableStreamCloseQueuedOrInFlight,
            ni = function() {
                function n() {
                    var i = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                    u = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
                    e = u.size,
                    t = u.highWaterMark,
                    r,
                    f;
                    if (y(this, n), this._state = "readable", this._reader = undefined, this._storedError = undefined, this._disturbed = !1, this._readableStreamController = undefined, r = i.type, f = String(r), f === "bytes") t === undefined && (t = 0),
                    this._readableStreamController = new ir(this, i, t);
                    else if (r === undefined) t === undefined && (t = 1),
                    this._readableStreamController = new nr(this, i, e, t);
                    else throw new RangeError("Invalid type is specified");
                }
                return v(n, [{
                    key: "cancel",
                    value: function(n) {
                        return u(this) === !1 ? Promise.reject(ut("cancel")) : h(this) === !0 ? Promise.reject(new TypeError("Cannot cancel a stream that already has a reader")) : w(this, n)
                    }
                },
                {
                    key: "getReader",
                    value: function() {
                        var t = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                        n = t.mode;
                        if (u(this) === !1) throw ut("getReader");
                        if (n === undefined) return ti(this);
                        if (n = String(n), n === "byob") return eu(this);
                        throw new RangeError("Invalid mode is specified");
                    }
                },
                {
                    key: "pipeThrough",
                    value: function(n, t) {
                        var i = n.writable,
                        r = n.readable,
                        u = this.pipeTo(i, t);
                        return sf(u),
                        r
                    }
                },
                {
                    key: "pipeTo",
                    value: function(n) {
                        var e = this,
                        c = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
                        l = c.preventClose,
                        a = c.preventAbort,
                        o = c.preventCancel;
                        if (u(this) === !1) return Promise.reject(ut("pipeTo"));
                        if (nu(n) === !1) return Promise.reject(new TypeError("ReadableStream.prototype.pipeTo's first argument must be a WritableStream"));
                        if (l = Boolean(l), a = Boolean(a), o = Boolean(o), h(this) === !0) return Promise.reject(new TypeError("ReadableStream.prototype.pipeTo cannot be used on a locked ReadableStream"));
                        if (tu(n) === !0) return Promise.reject(new TypeError("ReadableStream.prototype.pipeTo cannot be used on a locked WritableStream"));
                        var s = ti(this),
                        i = gr(n),
                        r = !1,
                        t = Promise.resolve();
                        return new Promise(function(u, h) {
                            function k() {
                                return (t = Promise.resolve(), r === !0) ? Promise.resolve() : i._readyPromise.then(function() {
                                    return ei(s).then(function(n) {
                                        var r = n.value,
                                        u = n.done;
                                        u !== !0 && (t = fu(i, r).
                                        catch(function() {}))
                                    })
                                }).then(k)
                            }
                            function b() {
                                var n = t;
                                return t.then(function() {
                                    return n !== t ? b() : undefined
                                })
                            }
                            function d(n, t, i) {
                                n._state === "errored" ? i(n._storedError) : t.
                                catch(i).
                                catch(f)
                            }
                            function g(n, t, i) {
                                n._state === "closed" ? i() : t.then(i).
                                catch(f)
                            }
                            function c(t, i, u) {
                                function e() {
                                    t().then(function() {
                                        return y(i, u)
                                    },
                                    function(n) {
                                        return y(!0, n)
                                    }).
                                    catch(f)
                                }
                                r !== !0 && (r = !0, n._state === "writable" && gt(n) === !1 ? b().then(e) : e())
                            }
                            function v(t, i) {
                                r !== !0 && (r = !0, n._state === "writable" && gt(n) === !1 ? b().then(function() {
                                    return y(t, i)
                                }).
                                catch(f) : y(t, i))
                            }
                            function y(n, t) {
                                uu(i);
                                fi(s);
                                n ? h(t) : u(undefined)
                            }
                            if (d(e, s._closedPromise,
                            function(t) {
                                a === !1 ? c(function() {
                                    return iu(n, t)
                                },
                                !0, t) : v(!0, t)
                            }), d(n, i._closedPromise,
                            function(n) {
                                o === !1 ? c(function() {
                                    return w(e, n)
                                },
                                !0, n) : v(!0, n)
                            }), g(e, s._closedPromise,
                            function() {
                                l === !1 ? c(function() {
                                    return ru(i)
                                }) : v()
                            }), gt(n) === !0 || n._state === "closed") {
                                var p = new TypeError("the destination writable stream closed before all data could be piped to it");
                                o === !1 ? c(function() {
                                    return w(e, p)
                                },
                                !0, p) : v(!0, p)
                            }
                            k().
                            catch(function(n) {
                                t = Promise.resolve();
                                f(n)
                            })
                        })
                    }
                },
                {
                    key: "tee",
                    value: function() {
                        if (u(this) === !1) throw ut("tee");
                        var n = su(this, !1);
                        return kt(n)
                    }
                },
                {
                    key: "locked",
                    get: function() {
                        if (u(this) === !1) throw ut("locked");
                        return h(this)
                    }
                }]),
                n
            } (),
            bi,
            ki,
            nr,
            tr,
            ir;
            n.exports = {
                ReadableStream: ni,
                IsReadableStreamDisturbed: ou,
                ReadableStreamDefaultControllerClose: ht,
                ReadableStreamDefaultControllerEnqueue: ct,
                ReadableStreamDefaultControllerError: tt,
                ReadableStreamDefaultControllerGetDesiredSize: oi
            };
            bi = function() {
                function n(t) {
                    if (y(this, n), u(t) === !1) throw new TypeError("ReadableStreamDefaultReader can only be constructed with a ReadableStream instance");
                    if (h(t) === !0) throw new TypeError("This stream has already been locked for exclusive reading by another reader");
                    di(this, t);
                    this._readRequests = []
                }
                return v(n, [{
                    key: "cancel",
                    value: function(n) {
                        return c(this) === !1 ? Promise.reject(yt("cancel")) : this._ownerReadableStream === undefined ? Promise.reject(vt("cancel")) : gi(this, n)
                    }
                },
                {
                    key: "read",
                    value: function() {
                        return c(this) === !1 ? Promise.reject(yt("read")) : this._ownerReadableStream === undefined ? Promise.reject(vt("read from")) : ei(this)
                    }
                },
                {
                    key: "releaseLock",
                    value: function() {
                        if (c(this) === !1) throw yt("releaseLock");
                        if (this._ownerReadableStream !== undefined) {
                            if (this._readRequests.length > 0) throw new TypeError("Tried to release a reader lock when that reader has pending read() calls un-settled");
                            fi(this)
                        }
                    }
                },
                {
                    key: "closed",
                    get: function() {
                        return c(this) === !1 ? Promise.reject(yt("closed")) : this._closedPromise
                    }
                }]),
                n
            } ();
            ki = function() {
                function n(t) {
                    if (y(this, n), !u(t)) throw new TypeError("ReadableStreamBYOBReader can only be constructed with a ReadableStream instance given a byte source");
                    if (b(t._readableStreamController) === !1) throw new TypeError("Cannot construct a ReadableStreamBYOBReader for a stream not constructed with a byte source");
                    if (h(t)) throw new TypeError("This stream has already been locked for exclusive reading by another reader");
                    di(this, t);
                    this._readIntoRequests = []
                }
                return v(n, [{
                    key: "cancel",
                    value: function(n) {
                        return a(this) ? this._ownerReadableStream === undefined ? Promise.reject(vt("cancel")) : gi(this, n) : Promise.reject(pt("cancel"))
                    }
                },
                {
                    key: "read",
                    value: function(n) {
                        return a(this) ? this._ownerReadableStream === undefined ? Promise.reject(vt("read from")) : ArrayBuffer.isView(n) ? n.byteLength === 0 ? Promise.reject(new TypeError("view must have non-zero byteLength")) : vu(this, n) : Promise.reject(new TypeError("view must be an array buffer view")) : Promise.reject(pt("read"))
                    }
                },
                {
                    key: "releaseLock",
                    value: function() {
                        if (!a(this)) throw pt("releaseLock");
                        if (this._ownerReadableStream !== undefined) {
                            if (this._readIntoRequests.length > 0) throw new TypeError("Tried to release a reader lock when that reader has pending read() calls un-settled");
                            fi(this)
                        }
                    }
                },
                {
                    key: "closed",
                    get: function() {
                        return a(this) ? this._closedPromise: Promise.reject(pt("closed"))
                    }
                }]),
                n
            } ();
            nr = function() {
                function n(t, i, e, o) {
                    var h, s, c;
                    if (y(this, n), u(t) === !1) throw new TypeError("ReadableStreamDefaultController can only be constructed with a ReadableStream instance");
                    if (t._readableStreamController !== undefined) throw new TypeError("ReadableStreamDefaultController instances can only be created by the ReadableStream constructor");
                    this._controlledReadableStream = t;
                    this._underlyingSource = i;
                    this._queue = undefined;
                    this._queueTotalSize = undefined;
                    p(this);
                    this._started = !1;
                    this._closeRequested = !1;
                    this._pullAgain = !1;
                    this._pulling = !1;
                    h = wr(e, o);
                    this._strategySize = h.size;
                    this._strategyHWM = h.highWaterMark;
                    s = this;
                    c = li(i, "start", [this]);
                    Promise.resolve(c).then(function() {
                        s._started = !0;
                        r(s._pulling === !1);
                        r(s._pullAgain === !1);
                        nt(s)
                    },
                    function(n) {
                        lt(s, n)
                    }).
                    catch(f)
                }
                return v(n, [{
                    key: "close",
                    value: function() {
                        if (st(this) === !1) throw wt("close");
                        if (this._closeRequested === !0) throw new TypeError("The stream has already been closed; do not close it again!");
                        var n = this._controlledReadableStream._state;
                        if (n !== "readable") throw new TypeError("The stream (in " + n + " state) is not in the readable state and cannot be closed");
                        ht(this)
                    }
                },
                {
                    key: "enqueue",
                    value: function(n) {
                        if (st(this) === !1) throw wt("enqueue");
                        if (this._closeRequested === !0) throw new TypeError("stream is closed or draining");
                        var t = this._controlledReadableStream._state;
                        if (t !== "readable") throw new TypeError("The stream (in " + t + " state) is not in the readable state and cannot be enqueued to");
                        return ct(this, n)
                    }
                },
                {
                    key: "error",
                    value: function(n) {
                        if (st(this) === !1) throw wt("error");
                        var t = this._controlledReadableStream;
                        if (t._state !== "readable") throw new TypeError("The stream is " + t._state + " and so cannot be errored");
                        tt(this, n)
                    }
                },
                {
                    key: "__cancelSteps",
                    value: function(n) {
                        return p(this),
                        et(this._underlyingSource, "cancel", [n])
                    }
                },
                {
                    key: "__pullSteps",
                    value: function() {
                        var n = this._controlledReadableStream,
                        t, i;
                        return this._queue.length > 0 ? (t = kr(this), this._closeRequested === !0 && this._queue.length === 0 ? d(n) : nt(this), Promise.resolve(o(t, !1))) : (i = yi(n), nt(this), i)
                    }
                },
                {
                    key: "desiredSize",
                    get: function() {
                        if (st(this) === !1) throw wt("desiredSize");
                        return oi(this)
                    }
                }]),
                n
            } ();
            tr = function() {
                function n(t, i) {
                    y(this, n);
                    this._associatedReadableByteStreamController = t;
                    this._view = i
                }
                return v(n, [{
                    key: "respond",
                    value: function(n) {
                        if (rr(this) === !1) throw vr("respond");
                        if (this._associatedReadableByteStreamController === undefined) throw new TypeError("This BYOB request has been invalidated");
                        nf(this._associatedReadableByteStreamController, n)
                    }
                },
                {
                    key: "respondWithNewView",
                    value: function(n) {
                        if (rr(this) === !1) throw vr("respond");
                        if (this._associatedReadableByteStreamController === undefined) throw new TypeError("This BYOB request has been invalidated");
                        if (!ArrayBuffer.isView(n)) throw new TypeError("You can only respond with array buffer views");
                        tf(this._associatedReadableByteStreamController, n)
                    }
                },
                {
                    key: "view",
                    get: function() {
                        return this._view
                    }
                }]),
                n
            } ();
            ir = function() {
                function n(t, i, e) {
                    var s, o, h;
                    if (y(this, n), u(t) === !1) throw new TypeError("ReadableByteStreamController can only be constructed with a ReadableStream instance given a byte source");
                    if (t._readableStreamController !== undefined) throw new TypeError("ReadableByteStreamController instances can only be created by the ReadableStream constructor given a byte source");
                    if (this._controlledReadableStream = t, this._underlyingByteSource = i, this._pullAgain = !1, this._pulling = !1, ur(this), this._queue = this._queueTotalSize = undefined, p(this), this._closeRequested = !1, this._started = !1, this._strategyHWM = br(e), s = i.autoAllocateChunkSize, s !== undefined && (Number.isInteger(s) === !1 || s <= 0)) throw new RangeError("autoAllocateChunkSize must be a positive integer");
                    this._autoAllocateChunkSize = s;
                    this._pendingPullIntos = [];
                    o = this;
                    h = li(i, "start", [this]);
                    Promise.resolve(h).then(function() {
                        o._started = !0;
                        r(o._pulling === !1);
                        r(o._pullAgain === !1);
                        it(o)
                    },
                    function(n) {
                        t._state === "readable" && rt(o, n)
                    }).
                    catch(f)
                }
                return v(n, [{
                    key: "close",
                    value: function() {
                        if (b(this) === !1) throw ft("close");
                        if (this._closeRequested === !0) throw new TypeError("The stream has already been closed; do not close it again!");
                        var n = this._controlledReadableStream._state;
                        if (n !== "readable") throw new TypeError("The stream (in " + n + " state) is not in the readable state and cannot be closed");
                        du(this)
                    }
                },
                {
                    key: "enqueue",
                    value: function(n) {
                        if (b(this) === !1) throw ft("enqueue");
                        if (this._closeRequested === !0) throw new TypeError("stream is closed or draining");
                        var t = this._controlledReadableStream._state;
                        if (t !== "readable") throw new TypeError("The stream (in " + t + " state) is not in the readable state and cannot be enqueued to");
                        if (!ArrayBuffer.isView(n)) throw new TypeError("You can only enqueue array buffer views when using a ReadableByteStreamController");
                        gu(this, n)
                    }
                },
                {
                    key: "error",
                    value: function(n) {
                        if (b(this) === !1) throw ft("error");
                        var t = this._controlledReadableStream;
                        if (t._state !== "readable") throw new TypeError("The stream is " + t._state + " and so cannot be errored");
                        rt(this, n)
                    }
                },
                {
                    key: "__cancelSteps",
                    value: function(n) {
                        if (this._pendingPullIntos.length > 0) {
                            var t = this._pendingPullIntos[0];
                            t.bytesFilled = 0
                        }
                        return p(this),
                        et(this._underlyingByteSource, "cancel", [n])
                    }
                },
                {
                    key: "__pullSteps",
                    value: function() {
                        var i = this._controlledReadableStream,
                        n, u, t, f, e, s;
                        if (r(ui(i) === !0), this._queueTotalSize > 0) {
                            r(g(i) === 0);
                            n = this._queue.shift();
                            this._queueTotalSize -= n.byteLength;
                            sr(this);
                            u = void 0;
                            try {
                                u = new Uint8Array(n.buffer, n.byteOffset, n.byteLength)
                            } catch(h) {
                                return Promise.reject(h)
                            }
                            return Promise.resolve(o(u, !1))
                        }
                        if (t = this._autoAllocateChunkSize, t !== undefined) {
                            f = void 0;
                            try {
                                f = new ArrayBuffer(t)
                            } catch(c) {
                                return Promise.reject(c)
                            }
                            e = {
                                buffer: f,
                                byteOffset: 0,
                                byteLength: t,
                                bytesFilled: 0,
                                elementSize: 1,
                                ctor: Uint8Array,
                                readerType: "default"
                            };
                            this._pendingPullIntos.push(e)
                        }
                        return s = yi(i),
                        it(this),
                        s
                    }
                },
                {
                    key: "byobRequest",
                    get: function() {
                        if (b(this) === !1) throw ft("byobRequest");
                        if (this._byobRequest === undefined && this._pendingPullIntos.length > 0) {
                            var n = this._pendingPullIntos[0],
                            t = new Uint8Array(n.buffer, n.byteOffset + n.bytesFilled, n.byteLength - n.bytesFilled);
                            this._byobRequest = new tr(this, t)
                        }
                        return this._byobRequest
                    }
                },
                {
                    key: "desiredSize",
                    get: function() {
                        if (b(this) === !1) throw ft("desiredSize");
                        return lr(this)
                    }
                }]),
                n
            } ()
        },
        function(n, t, i) {
            var f = i(6),
            u = i(4),
            r = i(2);
            t.TransformStream = f.TransformStream;
            t.ReadableStream = u.ReadableStream;
            t.IsReadableStreamDisturbed = u.IsReadableStreamDisturbed;
            t.ReadableStreamDefaultControllerClose = u.ReadableStreamDefaultControllerClose;
            t.ReadableStreamDefaultControllerEnqueue = u.ReadableStreamDefaultControllerEnqueue;
            t.ReadableStreamDefaultControllerError = u.ReadableStreamDefaultControllerError;
            t.ReadableStreamDefaultControllerGetDesiredSize = u.ReadableStreamDefaultControllerGetDesiredSize;
            t.AcquireWritableStreamDefaultWriter = r.AcquireWritableStreamDefaultWriter;
            t.IsWritableStream = r.IsWritableStream;
            t.IsWritableStreamLocked = r.IsWritableStreamLocked;
            t.WritableStream = r.WritableStream;
            t.WritableStreamAbort = r.WritableStreamAbort;
            t.WritableStreamDefaultControllerError = r.WritableStreamDefaultControllerError;
            t.WritableStreamDefaultWriterCloseWithErrorPropagation = r.WritableStreamDefaultWriterCloseWithErrorPropagation;
            t.WritableStreamDefaultWriterRelease = r.WritableStreamDefaultWriterRelease;
            t.WritableStreamDefaultWriterWrite = r.WritableStreamDefaultWriterWrite
        },
        function(n, t, i) {
            function e(n, t) {
                if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
            }
            function ct(n) {
                if (n._errored === !0) throw new TypeError("TransformStream is already errored");
                if (n._readableClosed === !0) throw new TypeError("Readable side is already closed");
                k(n)
            }
            function b(n, t) {
                var i, r, u;
                if (n._errored === !0) throw new TypeError("TransformStream is already errored");
                if (n._readableClosed === !0) throw new TypeError("Readable side is already closed");
                i = n._readableController;
                try {
                    et(i, t)
                } catch(f) {
                    n._readableClosed = !0;
                    a(n, f);
                    throw n._storedError;
                }
                r = l(i);
                u = r <= 0;
                u === !0 && n._backpressure === !1 && v(n, !0)
            }
            function lt(n, t) {
                if (n._errored === !0) throw new TypeError("TransformStream is already errored");
                s(n, t)
            }
            function k(n) {
                r(n._errored === !1);
                r(n._readableClosed === !1);
                try {
                    ft(n._readableController)
                } catch(t) {
                    r(!1)
                }
                n._readableClosed = !0
            }
            function a(n, t) {
                n._errored === !1 && s(n, t)
            }
            function s(n, t) {
                r(n._errored === !1);
                n._errored = !0;
                n._storedError = t;
                n._writableDone === !1 && ht(n._writableController, t);
                n._readableClosed === !1 && ot(n._readableController, t)
            }
            function d(n) {
                return (r(n._backpressureChangePromise !== undefined, "_backpressureChangePromise should have been initialized"), n._backpressure === !1) ? Promise.resolve() : (r(n._backpressure === !0, "_backpressure should have been initialized"), n._backpressureChangePromise)
            }
            function v(n, t) {
                r(n._backpressure !== t, "TransformStreamSetBackpressure() should be called only when backpressure is changed");
                n._backpressureChangePromise !== undefined && n._backpressureChangePromise_resolve(t);
                n._backpressureChangePromise = new Promise(function(t) {
                    n._backpressureChangePromise_resolve = t
                });
                n._backpressureChangePromise.then(function(n) {
                    r(n !== t, "_backpressureChangePromise should be fulfilled only when backpressure is changed")
                });
                n._backpressure = t
            }
            function at(n, t) {
                var i = t._controlledTransformStream;
                return b(i, n),
                Promise.resolve()
            }
            function vt(n, t) {
                r(n._errored === !1);
                r(n._transforming === !1);
                r(n._backpressure === !1);
                n._transforming = !0;
                var u = n._transformer,
                i = n._transformStreamController,
                f = it(u, "transform", [t, i], at, [t, i]);
                return f.then(function() {
                    return n._transforming = !1,
                    d(n)
                },
                function(t) {
                    return a(n, t),
                    Promise.reject(t)
                })
            }
            function h(n) {
                return p(n) ? Object.prototype.hasOwnProperty.call(n, "_controlledTransformStream") ? !0 : !1 : !1
            }
            function y(n) {
                return p(n) ? Object.prototype.hasOwnProperty.call(n, "_transformStreamController") ? !0 : !1 : !1
            }
            function c(n) {
                return new TypeError("TransformStreamDefaultController.prototype." + n + " can only be used on a TransformStreamDefaultController")
            }
            function g(n) {
                return new TypeError("TransformStream.prototype." + n + " can only be used on a TransformStream")
            }
            var f = function() {
                function n(n, t) {
                    for (var i, r = 0; r < t.length; r++) i = t[r],
                    i.enumerable = i.enumerable || !1,
                    i.configurable = !0,
                    "value" in i && (i.writable = !0),
                    Object.defineProperty(n, i.key, i)
                }
                return function(t, i, r) {
                    return i && n(t.prototype, i),
                    r && n(t, r),
                    t
                }
            } (),
            nt = i(1),
            r = nt.assert,
            o = i(0),
            tt = o.InvokeOrNoop,
            it = o.PromiseInvokeOrPerformFallback,
            rt = o.PromiseInvokeOrNoop,
            p = o.typeIsObject,
            u = i(4),
            ut = u.ReadableStream,
            ft = u.ReadableStreamDefaultControllerClose,
            et = u.ReadableStreamDefaultControllerEnqueue,
            ot = u.ReadableStreamDefaultControllerError,
            l = u.ReadableStreamDefaultControllerGetDesiredSize,
            w = i(2),
            st = w.WritableStream,
            ht = w.WritableStreamDefaultControllerError,
            yt = function() {
                function n(t, i) {
                    e(this, n);
                    this._transformStream = t;
                    this._startPromise = i
                }
                return f(n, [{
                    key: "start",
                    value: function(n) {
                        var t = this._transformStream;
                        return t._writableController = n,
                        this._startPromise.then(function() {
                            return d(t)
                        })
                    }
                },
                {
                    key: "write",
                    value: function(n) {
                        var t = this._transformStream;
                        return vt(t, n)
                    }
                },
                {
                    key: "abort",
                    value: function() {
                        var n = this._transformStream;
                        n._writableDone = !0;
                        s(n, new TypeError("Writable side aborted"))
                    }
                },
                {
                    key: "close",
                    value: function() {
                        var n = this._transformStream,
                        t;
                        return r(n._transforming === !1),
                        n._writableDone = !0,
                        t = rt(n._transformer, "flush", [n._transformStreamController]),
                        t.then(function() {
                            return n._errored === !0 ? Promise.reject(n._storedError) : (n._readableClosed === !1 && k(n), Promise.resolve())
                        }).
                        catch(function(t) {
                            return a(n, t),
                            Promise.reject(n._storedError)
                        })
                    }
                }]),
                n
            } (),
            pt = function() {
                function n(t, i) {
                    e(this, n);
                    this._transformStream = t;
                    this._startPromise = i
                }
                return f(n, [{
                    key: "start",
                    value: function(n) {
                        var t = this._transformStream;
                        return t._readableController = n,
                        this._startPromise.then(function() {
                            return (r(t._backpressureChangePromise !== undefined, "_backpressureChangePromise should have been initialized"), t._backpressure === !0) ? Promise.resolve() : (r(t._backpressure === !1, "_backpressure should have been initialized"), t._backpressureChangePromise)
                        })
                    }
                },
                {
                    key: "pull",
                    value: function() {
                        var n = this._transformStream;
                        return r(n._backpressure === !0, "pull() should be never called while _backpressure is false"),
                        r(n._backpressureChangePromise !== undefined, "_backpressureChangePromise should have been initialized"),
                        v(n, !1),
                        n._backpressureChangePromise
                    }
                },
                {
                    key: "cancel",
                    value: function() {
                        var n = this._transformStream;
                        n._readableClosed = !0;
                        s(n, new TypeError("Readable side canceled"))
                    }
                }]),
                n
            } (),
            wt = function() {
                function n(t) {
                    if (e(this, n), y(t) === !1) throw new TypeError("TransformStreamDefaultController can only be constructed with a TransformStream instance");
                    if (t._transformStreamController !== undefined) throw new TypeError("TransformStreamDefaultController instances can only be created by the TransformStream constructor");
                    this._controlledTransformStream = t
                }
                return f(n, [{
                    key: "enqueue",
                    value: function(n) {
                        if (h(this) === !1) throw c("enqueue");
                        b(this._controlledTransformStream, n)
                    }
                },
                {
                    key: "close",
                    value: function() {
                        if (h(this) === !1) throw c("close");
                        ct(this._controlledTransformStream)
                    }
                },
                {
                    key: "error",
                    value: function(n) {
                        if (h(this) === !1) throw c("error");
                        lt(this._controlledTransformStream, n)
                    }
                },
                {
                    key: "desiredSize",
                    get: function() {
                        if (h(this) === !1) throw c("desiredSize");
                        var n = this._controlledTransformStream,
                        t = n._readableController;
                        return l(t)
                    }
                }]),
                n
            } (),
            bt = function() {
                function n() {
                    var i = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                    f,
                    o,
                    h,
                    c,
                    t,
                    a;
                    e(this, n);
                    this._transformer = i;
                    f = i.readableStrategy;
                    o = i.writableStrategy;
                    this._transforming = !1;
                    this._errored = !1;
                    this._storedError = undefined;
                    this._writableController = undefined;
                    this._readableController = undefined;
                    this._transformStreamController = undefined;
                    this._writableDone = !1;
                    this._readableClosed = !1;
                    this._backpressure = undefined;
                    this._backpressureChangePromise = undefined;
                    this._backpressureChangePromise_resolve = undefined;
                    this._transformStreamController = new wt(this);
                    var s = void 0,
                    u = new Promise(function(n) {
                        s = n
                    }),
                    y = new pt(this, u);
                    this._readable = new ut(y, f);
                    h = new yt(this, u);
                    this._writable = new st(h, o);
                    r(this._writableController !== undefined);
                    r(this._readableController !== undefined);
                    c = l(this._readableController);
                    v(this, c <= 0);
                    t = this;
                    a = tt(i, "start", [t._transformStreamController]);
                    s(a);
                    u.
                    catch(function(n) {
                        t._errored === !1 && (t._errored = !0, t._storedError = n)
                    })
                }
                return f(n, [{
                    key: "readable",
                    get: function() {
                        if (y(this) === !1) throw g("readable");
                        return this._readable
                    }
                },
                {
                    key: "writable",
                    get: function() {
                        if (y(this) === !1) throw g("writable");
                        return this._writable
                    }
                }]),
                n
            } ();
            n.exports = {
                TransformStream: bt
            }
        },
        function(n, t, i) {
            n.exports = i(5)
        }]))
    },
    function(n, t, i) {
        "use strict";
        function v(n) {
            return n && n.__esModule ? n: {
                "default": n
            }
        }
        var r;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.PDFJS = t.globalScope = undefined;
        var f = i(8),
        u = i(0),
        e = i(57),
        s = i(60),
        h = i(14),
        o = v(h),
        c = i(59),
        l = i(61),
        a = i(62);
        o.
    default.PDFJS || (o.
    default.PDFJS = {});
        r = o.
    default.PDFJS;
        r.version = "2.0.104";
        r.build = "012d0756";
        r.pdfBug = !1;
        r.verbosity !== undefined && u.setVerbosityLevel(r.verbosity);
        delete r.verbosity;
        Object.defineProperty(r, "verbosity", {
            get: function() {
                return u.getVerbosityLevel()
            },
            set: function(n) {
                u.setVerbosityLevel(n)
            },
            enumerable: !0,
            configurable: !0
        });
        r.VERBOSITY_LEVELS = u.VERBOSITY_LEVELS;
        r.OPS = u.OPS;
        r.UNSUPPORTED_FEATURES = u.UNSUPPORTED_FEATURES;
        r.isValidUrl = f.isValidUrl;
        r.shadow = u.shadow;
        r.createBlob = u.createBlob;
        r.createObjectURL = function(n, t) {
            return u.createObjectURL(n, t, r.disableCreateObjectURL)
        };
        Object.defineProperty(r, "isLittleEndian", {
            configurable: !0,
            get: function() {
                return u.shadow(r, "isLittleEndian", u.isLittleEndian())
            }
        });
        r.removeNullCharacters = u.removeNullCharacters;
        r.PasswordResponses = u.PasswordResponses;
        r.PasswordException = u.PasswordException;
        r.UnknownErrorException = u.UnknownErrorException;
        r.InvalidPDFException = u.InvalidPDFException;
        r.MissingPDFException = u.MissingPDFException;
        r.UnexpectedResponseException = u.UnexpectedResponseException;
        r.Util = u.Util;
        r.PageViewport = u.PageViewport;
        r.createPromiseCapability = u.createPromiseCapability;
        r.maxImageSize = r.maxImageSize === undefined ? -1 : r.maxImageSize;
        r.cMapUrl = r.cMapUrl === undefined ? null: r.cMapUrl;
        r.cMapPacked = r.cMapPacked === undefined ? !1 : r.cMapPacked;
        r.disableFontFace = r.disableFontFace === undefined ? !1 : r.disableFontFace;
        r.imageResourcesPath = r.imageResourcesPath === undefined ? "": r.imageResourcesPath;
        r.disableWorker = r.disableWorker === undefined ? !1 : r.disableWorker;
        r.workerSrc = r.workerSrc === undefined ? null: r.workerSrc;
        r.workerPort = r.workerPort === undefined ? null: r.workerPort;
        r.disableRange = r.disableRange === undefined ? !1 : r.disableRange;
        r.disableStream = r.disableStream === undefined ? !1 : r.disableStream;
        r.disableAutoFetch = r.disableAutoFetch === undefined ? !1 : r.disableAutoFetch;
        r.pdfBug = r.pdfBug === undefined ? !1 : r.pdfBug;
        r.postMessageTransfers = r.postMessageTransfers === undefined ? !0 : r.postMessageTransfers;
        r.disableCreateObjectURL = r.disableCreateObjectURL === undefined ? !1 : r.disableCreateObjectURL;
        r.disableWebGL = r.disableWebGL === undefined ? !0 : r.disableWebGL;
        r.externalLinkTarget = r.externalLinkTarget === undefined ? f.LinkTarget.NONE: r.externalLinkTarget;
        r.externalLinkRel = r.externalLinkRel === undefined ? f.DEFAULT_LINK_REL: r.externalLinkRel;
        r.isEvalSupported = r.isEvalSupported === undefined ? !0 : r.isEvalSupported;
        r.getDocument = e.getDocument;
        r.LoopbackPort = e.LoopbackPort;
        r.PDFDataRangeTransport = e.PDFDataRangeTransport;
        r.PDFWorker = e.PDFWorker;
        r.CustomStyle = f.CustomStyle;
        r.LinkTarget = f.LinkTarget;
        r.addLinkAttributes = f.addLinkAttributes;
        r.getFilenameFromUrl = f.getFilenameFromUrl;
        r.isExternalLinkTargetSet = f.isExternalLinkTargetSet;
        r.AnnotationLayer = s.AnnotationLayer;
        r.renderTextLayer = l.renderTextLayer;
        r.Metadata = c.Metadata;
        r.SVGGraphics = a.SVGGraphics;
        t.globalScope = o.
    default;
        t.PDFJS = r
    },
    function(n, t, i) {
        "use strict";
        function r(n) {
            this.docId = n;
            this.styleElement = null;
            this.nativeFontFaces = [];
            this.loadTestFontId = 0;
            this.loadingContext = {
                requests: [],
                nextRequestId: 0
            }
        }
        var u, f, e, o, s;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.FontLoader = t.FontFaceObject = undefined;
        u = i(0);
        r.prototype = {
            insertRule: function(n) {
                var t = this.styleElement,
                i;
                t || (t = this.styleElement = document.createElement("style"), t.id = "PDFJS_FONT_STYLE_TAG_" + this.docId, document.documentElement.getElementsByTagName("head")[0].appendChild(t));
                i = t.sheet;
                i.insertRule(n, i.cssRules.length)
            },
            clear: function() {
                this.styleElement && (this.styleElement.remove(), this.styleElement = null);
                this.nativeFontFaces.forEach(function(n) {
                    document.fonts.delete(n)
                });
                this.nativeFontFaces.length = 0
            }
        };
        f = function() {
            return atob("T1RUTwALAIAAAwAwQ0ZGIDHtZg4AAAOYAAAAgUZGVE1lkzZwAAAEHAAAABxHREVGABQAFQAABDgAAAAeT1MvMlYNYwkAAAEgAAAAYGNtYXABDQLUAAACNAAAAUJoZWFk/xVFDQAAALwAAAA2aGhlYQdkA+oAAAD0AAAAJGhtdHgD6AAAAAAEWAAAAAZtYXhwAAJQAAAAARgAAAAGbmFtZVjmdH4AAAGAAAAAsXBvc3T/hgAzAAADeAAAACAAAQAAAAEAALZRFsRfDzz1AAsD6AAAAADOBOTLAAAAAM4KHDwAAAAAA+gDIQAAAAgAAgAAAAAAAAABAAADIQAAAFoD6AAAAAAD6AABAAAAAAAAAAAAAAAAAAAAAQAAUAAAAgAAAAQD6AH0AAUAAAKKArwAAACMAooCvAAAAeAAMQECAAACAAYJAAAAAAAAAAAAAQAAAAAAAAAAAAAAAFBmRWQAwAAuAC4DIP84AFoDIQAAAAAAAQAAAAAAAAAAACAAIAABAAAADgCuAAEAAAAAAAAAAQAAAAEAAAAAAAEAAQAAAAEAAAAAAAIAAQAAAAEAAAAAAAMAAQAAAAEAAAAAAAQAAQAAAAEAAAAAAAUAAQAAAAEAAAAAAAYAAQAAAAMAAQQJAAAAAgABAAMAAQQJAAEAAgABAAMAAQQJAAIAAgABAAMAAQQJAAMAAgABAAMAAQQJAAQAAgABAAMAAQQJAAUAAgABAAMAAQQJAAYAAgABWABYAAAAAAAAAwAAAAMAAAAcAAEAAAAAADwAAwABAAAAHAAEACAAAAAEAAQAAQAAAC7//wAAAC7////TAAEAAAAAAAABBgAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAD/gwAyAAAAAQAAAAAAAAAAAAAAAAAAAAABAAQEAAEBAQJYAAEBASH4DwD4GwHEAvgcA/gXBIwMAYuL+nz5tQXkD5j3CBLnEQACAQEBIVhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYAAABAQAADwACAQEEE/t3Dov6fAH6fAT+fPp8+nwHDosMCvm1Cvm1DAz6fBQAAAAAAAABAAAAAMmJbzEAAAAAzgTjFQAAAADOBOQpAAEAAAAAAAAADAAUAAQAAAABAAAAAgABAAAAAAAAAAAD6AAAAAAAAA==")
        };
        Object.defineProperty(r.prototype, "loadTestFont", {
            get: function() {
                return u.shadow(this, "loadTestFont", f())
            },
            configurable: !0
        });
        r.prototype.addNativeFontFace = function(n) {
            this.nativeFontFaces.push(n);
            document.fonts.add(n)
        };
        r.prototype.bind = function(n, t) {
            for (var i, f, e, o, s = [], c = [], l = [], v = function(n) {
                return n.loaded.
                catch(function(t) {
                    u.warn('Failed to load font "' + n.family + '": ' + t)
                })
            },
            a = r.isFontLoadingAPISupported && !r.isSyncFontLoadingSupported, h = 0, y = n.length; h < y; h++)(i = n[h], i.attached || i.loading === !1) || (i.attached = !0, a ? (f = i.createNativeFontFace(), f && (this.addNativeFontFace(f), l.push(v(f)))) : (e = i.createFontFaceRule(), e && (this.insertRule(e), s.push(e), c.push(i))));
            o = this.queueLoadingCallback(t);
            a ? Promise.all(l).then(function() {
                o.complete()
            }) : s.length > 0 && !r.isSyncFontLoadingSupported ? this.prepareFontLoadEvent(s, c, o) : o.complete()
        };
        r.prototype.queueLoadingCallback = function(n) {
            function r() {
                for (u.assert(!i.end, "completeRequest() cannot be called twice"), i.end = Date.now(); t.requests.length > 0 && t.requests[0].end;) {
                    var n = t.requests.shift();
                    setTimeout(n.callback, 0)
                }
            }
            var t = this.loadingContext,
            f = "pdfjs-font-loading-" + t.nextRequestId++,
            i = {
                id: f,
                complete: r,
                callback: n,
                started: Date.now()
            };
            return t.requests.push(i),
            i
        };
        r.prototype.prepareFontLoadEvent = function(n, t, i) {
            function v(n, t) {
                return n.charCodeAt(t) << 24 | n.charCodeAt(t + 1) << 16 | n.charCodeAt(t + 2) << 8 | n.charCodeAt(t + 3) & 255
            }
            function w(n, t, i, r) {
                var u = n.substr(0, t),
                f = n.substr(t + i);
                return u + r + f
            }
            function b(n, t) {
                if (p++, p > 30) {
                    u.warn("Load test font never loaded.");
                    t();
                    return
                }
                l.font = "30px " + n;
                l.fillText(".", 0, 20);
                var i = l.getImageData(0, 0, 1, 1);
                if (i.data[3] > 0) {
                    t();
                    return
                }
                setTimeout(b.bind(null, n, t))
            }
            var r, e, y = document.createElement("canvas"),
            l,
            p,
            g,
            nt,
            h,
            c,
            a;
            y.width = 1;
            y.height = 1;
            l = y.getContext("2d");
            p = 0;
            var f = "lt" + Date.now() + this.loadTestFontId++,
            o = this.loadTestFont;
            o = w(o, 976, f.length, f);
            var k = 16,
            d = 1482184792,
            s = v(o, k);
            for (r = 0, e = f.length - 3; r < e; r += 4) s = s - d + v(f, r) | 0;
            for (r < f.length && (s = s - d + v(f + "XXX", r) | 0), o = w(o, k, 4, u.string32(s)), g = "url(data:font/opentype;base64," + btoa(o) + ");", nt = '@font-face { font-family:"' + f + '";src:' + g + "}", this.insertRule(nt), h = [], r = 0, e = t.length; r < e; r++) h.push(t[r].loadedName);
            for (h.push(f), c = document.createElement("div"), c.setAttribute("style", "visibility: hidden;width: 10px; height: 10px;position: absolute; top: 0px; left: 0px;"), r = 0, e = h.length; r < e; ++r) a = document.createElement("span"),
            a.textContent = "Hi",
            a.style.fontFamily = h[r],
            c.appendChild(a);
            document.body.appendChild(c);
            b(f,
            function() {
                document.body.removeChild(c);
                i.complete()
            })
        };
        r.isFontLoadingAPISupported = typeof document != "undefined" && !!document.fonts;
        e = function() {
            if (typeof navigator == "undefined") return ! 0;
            var n = !1,
            t = /Mozilla\/5.0.*?rv:(\d+).*? Gecko/.exec(navigator.userAgent);
            return t && t[1] >= 14 && (n = !0),
            n
        };
        Object.defineProperty(r, "isSyncFontLoadingSupported", {
            get: function() {
                return u.shadow(r, "isSyncFontLoadingSupported", e())
            },
            enumerable: !0,
            configurable: !0
        });
        o = {
            get value() {
                return u.shadow(this, "value", u.isEvalSupported())
            }
        };
        s = function() {
            function n(n, t) {
                this.compiledGlyphs = Object.create(null);
                for (var i in n) this[i] = n[i];
                this.options = t
            }
            return n.prototype = {
                createNativeFontFace: function() {
                    if (!this.data) return null;
                    if (this.options.disableFontFace) return this.disableFontFace = !0,
                    null;
                    var n = new FontFace(this.loadedName, this.data, {});
                    return this.options.fontRegistry && this.options.fontRegistry.registerFont(this),
                    n
                },
                createFontFaceRule: function() {
                    if (!this.data) return null;
                    if (this.options.disableFontFace) return this.disableFontFace = !0,
                    null;
                    var t = u.bytesToString(new Uint8Array(this.data)),
                    i = this.loadedName,
                    n = "url(data:" + this.mimetype + ";base64," + btoa(t) + ");",
                    r = '@font-face { font-family:"' + i + '";src:' + n + "}";
                    return this.options.fontRegistry && this.options.fontRegistry.registerFont(this, n),
                    r
                },
                getPathGenerator: function(n, t) {
                    var u, i, r, f, s, e;
                    if (! (t in this.compiledGlyphs)) if (u = n.get(this.loadedName + "_path_" + t), this.options.isEvalSupported && o.value) {
                        for (e = "", r = 0, f = u.length; r < f; r++) i = u[r],
                        s = i.args !== undefined ? i.args.join(",") : "",
                        e += "c." + i.cmd + "(" + s + ");\n";
                        this.compiledGlyphs[t] = new Function("c", "size", e)
                    } else this.compiledGlyphs[t] = function(n, t) {
                        for (r = 0, f = u.length; r < f; r++) i = u[r],
                        i.cmd === "scale" && (i.args = [t, -t]),
                        n[i.cmd].apply(n, i.args)
                    };
                    return this.compiledGlyphs[t]
                }
            },
            n
        } ();
        t.FontFaceObject = s;
        t.FontLoader = r
    },
    function(n, t, i) {
        "use strict";
        function y(n) {
            n.mozCurrentTransform || (n._originalSave = n.save, n._originalRestore = n.restore, n._originalRotate = n.rotate, n._originalScale = n.scale, n._originalTranslate = n.translate, n._originalTransform = n.transform, n._originalSetTransform = n.setTransform, n._transformMatrix = n._transformMatrix || [1, 0, 0, 1, 0, 0], n._transformStack = [], Object.defineProperty(n, "mozCurrentTransform", {
                get: function() {
                    return this._transformMatrix
                }
            }), Object.defineProperty(n, "mozCurrentTransformInverse", {
                get: function() {
                    var n = this._transformMatrix,
                    t = n[0],
                    i = n[1],
                    r = n[2],
                    u = n[3],
                    o = n[4],
                    s = n[5],
                    f = t * u - i * r,
                    e = i * r - t * u;
                    return [u / f, i / e, r / e, t / f, (u * o - r * s) / e, (i * o - t * s) / f]
                }
            }), n.save = function() {
                var n = this._transformMatrix;
                this._transformStack.push(n);
                this._transformMatrix = n.slice(0, 6);
                this._originalSave()
            },
            n.restore = function() {
                var n = this._transformStack.pop();
                n && (this._transformMatrix = n, this._originalRestore())
            },
            n.translate = function(n, t) {
                var i = this._transformMatrix;
                i[4] = i[0] * n + i[2] * t + i[4];
                i[5] = i[1] * n + i[3] * t + i[5];
                this._originalTranslate(n, t)
            },
            n.scale = function(n, t) {
                var i = this._transformMatrix;
                i[0] = i[0] * n;
                i[1] = i[1] * n;
                i[2] = i[2] * t;
                i[3] = i[3] * t;
                this._originalScale(n, t)
            },
            n.transform = function(t, i, r, u, f, e) {
                var o = this._transformMatrix;
                this._transformMatrix = [o[0] * t + o[2] * i, o[1] * t + o[3] * i, o[0] * r + o[2] * u, o[1] * r + o[3] * u, o[0] * f + o[2] * e + o[4], o[1] * f + o[3] * e + o[5]];
                n._originalTransform(t, i, r, u, f, e)
            },
            n.setTransform = function(t, i, r, u, f, e) {
                this._transformMatrix = [t, i, r, u, f, e];
                n._originalSetTransform(t, i, r, u, f, e)
            },
            n.rotate = function(n) {
                var i = Math.cos(n),
                r = Math.sin(n),
                t = this._transformMatrix;
                this._transformMatrix = [t[0] * i + t[2] * r, t[1] * i + t[3] * r, t[0] * -r + t[2] * i, t[1] * -r + t[3] * i, t[4], t[5]];
                this._originalRotate(n)
            })
        }
        function k(n) {
            for (var g = 1e3,
            c = n.width,
            l = n.height,
            f, h, s = c + 1,
            e = new Uint8Array(s * (l + 1)), nt = new Uint8Array([0, 2, 4, 0, 1, 0, 5, 4, 8, 10, 0, 8, 0, 2, 1, 0]), a = c + 7 & -8, tt = n.data, i = new Uint8Array(a * l), t = 0, w, rt, o, y, ut, b, r, k, ft, u = 0, it = tt.length; u < it; u++) for (w = 128, rt = tt[u]; w > 0;) i[t++] = rt & w ? 0 : 255,
            w >>= 1;
            for (o = 0, t = 0, i[t] !== 0 && (e[0] = 1, ++o), f = 1; f < c; f++) i[t] !== i[t + 1] && (e[f] = i[t] ? 2 : 1, ++o),
            t++;
            for (i[t] !== 0 && (e[f] = 2, ++o), u = 1; u < l; u++) {
                for (t = u * a, h = u * s, i[t - a] !== i[t] && (e[h] = i[t] ? 1 : 8, ++o), y = (i[t] ? 4 : 0) + (i[t - a] ? 8 : 0), f = 1; f < c; f++) y = (y >> 2) + (i[t + 1] ? 4 : 0) + (i[t - a + 1] ? 8 : 0),
                nt[y] && (e[h + f] = nt[y], ++o),
                t++;
                if (i[t - a] !== i[t] && (e[h + f] = i[t] ? 2 : 4, ++o), o > g) return null
            }
            for (t = a * (l - 1), h = u * s, i[t] !== 0 && (e[h] = 8, ++o), f = 1; f < c; f++) i[t] !== i[t + 1] && (e[h + f] = i[t] ? 4 : 8, ++o),
            t++;
            if (i[t] !== 0 && (e[h + f] = 4, ++o), o > g) return null;
            for (ut = new Int32Array([0, s, -1, 0, -s, 0, 0, 0, 1]), b = [], u = 0; o && u <= l; u++) {
                for (r = u * s, k = r + c; r < k && !e[r];) r++;
                if (r !== k) {
                    var d = [r % s, u],
                    v = e[r],
                    et = r,
                    p;
                    do {
                        ft = ut[v];
                        do r += ft;
                        while (!e[r]);
                        p = e[r];
                        p !== 5 && p !== 10 ? (v = p, e[r] = 0) : (v = p & 51 * v >> 4, e[r] &= v >> 2 | v << 2);
                        d.push(r % s);
                        d.push(r / s | 0); --o
                    } while ( et !== r );
                    b.push(d); --u
                }
            }
            return function(n) {
                var r, u, t, i, f;
                for (n.save(), n.scale(1 / c, -1 / l), n.translate(0, -l), n.beginPath(), r = 0, u = b.length; r < u; r++) for (t = b[r], n.moveTo(t[0], t[1]), i = 2, f = t.length; i < f; i += 2) n.lineTo(t[i], t[i + 1]);
                n.fill();
                n.beginPath();
                n.restore()
            }
        }
        var p, s, w;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.CanvasGraphics = undefined;
        var r = i(0),
        e = i(116),
        o = i(58),
        h = 16,
        c = 100,
        f = 4096,
        l = .65,
        b = !0,
        a = 1e3,
        u = 16,
        v = {
            get value() {
                return r.shadow(v, "value", r.isLittleEndian())
            }
        };
        p = function() {
            function n(n) {
                this.canvasFactory = n;
                this.cache = Object.create(null)
            }
            return n.prototype = {
                getCanvas: function(n, t, i, r) {
                    var u;
                    return this.cache[n] !== undefined ? (u = this.cache[n], this.canvasFactory.reset(u, t, i), u.context.setTransform(1, 0, 0, 1, 0, 0)) : (u = this.canvasFactory.create(t, i), this.cache[n] = u),
                    r && y(u.context),
                    u
                },
                clear: function() {
                    var n, t;
                    for (n in this.cache) t = this.cache[n],
                    this.canvasFactory.destroy(t),
                    delete this.cache[n]
                }
            },
            n
        } ();
        s = function() {
            function n() {
                this.alphaIsShape = !1;
                this.fontSize = 0;
                this.fontSizeScale = 1;
                this.textMatrix = r.IDENTITY_MATRIX;
                this.textMatrixScale = 1;
                this.fontMatrix = r.FONT_IDENTITY_MATRIX;
                this.leading = 0;
                this.x = 0;
                this.y = 0;
                this.lineX = 0;
                this.lineY = 0;
                this.charSpacing = 0;
                this.wordSpacing = 0;
                this.textHScale = 1;
                this.textRenderingMode = r.TextRenderingMode.FILL;
                this.textRise = 0;
                this.fillColor = "#000000";
                this.strokeColor = "#000000";
                this.patternFill = !1;
                this.fillAlpha = 1;
                this.strokeAlpha = 1;
                this.lineWidth = 1;
                this.activeSMask = null;
                this.resumeSMaskCtx = null
            }
            return n.prototype = {
                clone: function() {
                    return Object.create(this)
                },
                setCurrentPoint: function(n, t) {
                    this.x = n;
                    this.y = t
                }
            },
            n
        } ();
        w = function() {
            function n(n, t, i, r, u) {
                this.ctx = n;
                this.current = new s;
                this.stateStack = [];
                this.pendingClip = null;
                this.pendingEOFill = !1;
                this.res = null;
                this.xobjs = null;
                this.commonObjs = t;
                this.objs = i;
                this.canvasFactory = r;
                this.imageLayer = u;
                this.groupStack = [];
                this.processingType3 = null;
                this.baseTransform = null;
                this.baseTransformStack = [];
                this.groupLevel = 0;
                this.smaskStack = [];
                this.smaskCounter = 0;
                this.tempSMask = null;
                this.cachedCanvases = new p(this.canvasFactory);
                n && y(n);
                this.cachedGetSinglePixelWidth = null
            }
            function g(n, t) {
                if (typeof ImageData != "undefined" && t instanceof ImageData) {
                    n.putImageData(t, 0, 0);
                    return
                }
                var rt = t.height,
                w = t.width,
                k = rt % u,
                d = (rt - k) / u,
                it = k === 0 ? d: d + 1,
                g = n.createImageData(w, u),
                e = 0,
                i,
                a = t.data,
                y = g.data,
                f,
                h,
                b,
                p;
                if (t.kind === r.ImageKind.GRAYSCALE_1BPP) {
                    var et = a.byteLength,
                    o = new Uint32Array(y.buffer, 0, y.byteLength >> 2),
                    ot = o.length,
                    st = w + 7 >> 3,
                    c = 4294967295,
                    l = v.value ? 4278190080 : 255;
                    for (f = 0; f < it; f++) {
                        for (b = f < d ? u: k, i = 0, h = 0; h < b; h++) {
                            for (var ut = et - e,
                            nt = 0,
                            ft = ut > st ? w: ut * 8 - 7, ht = ft & -8, tt = 0, s = 0; nt < ht; nt += 8) s = a[e++],
                            o[i++] = s & 128 ? c: l,
                            o[i++] = s & 64 ? c: l,
                            o[i++] = s & 32 ? c: l,
                            o[i++] = s & 16 ? c: l,
                            o[i++] = s & 8 ? c: l,
                            o[i++] = s & 4 ? c: l,
                            o[i++] = s & 2 ? c: l,
                            o[i++] = s & 1 ? c: l;
                            for (; nt < ft; nt++) tt === 0 && (s = a[e++], tt = 128),
                            o[i++] = s & tt ? c: l,
                            tt >>= 1
                        }
                        while (i < ot) o[i++] = 0;
                        n.putImageData(g, 0, f * u)
                    }
                } else if (t.kind === r.ImageKind.RGBA_32BPP) {
                    for (h = 0, p = w * u * 4, f = 0; f < d; f++) y.set(a.subarray(e, e + p)),
                    e += p,
                    n.putImageData(g, 0, h),
                    h += u;
                    f < it && (p = w * k * 4, y.set(a.subarray(e, e + p)), n.putImageData(g, 0, h))
                } else if (t.kind === r.ImageKind.RGB_24BPP) for (b = u, p = w * b, f = 0; f < it; f++) {
                    for (f >= d && (b = k, p = w * b), i = 0, h = p; h--;) y[i++] = a[e++],
                    y[i++] = a[e++],
                    y[i++] = a[e++],
                    y[i++] = 255;
                    n.putImageData(g, 0, f * u)
                } else throw new Error("bad image kind: " + t.kind);
            }
            function i(n, t) {
                for (var v, o, s, i, h, y, c = t.height,
                l = t.width,
                f = c % u,
                e = (c - f) / u, p = f === 0 ? e: e + 1, a = n.createImageData(l, u), w = 0, b = t.data, k = a.data, r = 0; r < p; r++) {
                    for (v = r < e ? u: f, o = 3, s = 0; s < v; s++) for (i = 0, h = 0; h < l; h++) i || (y = b[w++], i = 128),
                    k[o] = y & i ? 0 : 255,
                    o += 4,
                    i >>= 1;
                    n.putImageData(a, 0, r * u)
                }
            }
            function t(n, t) {
                for (var i, u = ["strokeStyle", "fillStyle", "fillRule", "globalAlpha", "lineWidth", "lineCap", "lineJoin", "miterLimit", "globalCompositeOperation", "font"], r = 0, f = u.length; r < f; r++) i = u[r],
                n[i] !== undefined && (t[i] = n[i]);
                n.setLineDash !== undefined && (t.setLineDash(n.getLineDash()), t.lineDashOffset = n.lineDashOffset)
            }
            function nt(n) {
                n.strokeStyle = "#000000";
                n.fillStyle = "#000000";
                n.fillRule = "nonzero";
                n.globalAlpha = 1;
                n.lineWidth = 1;
                n.lineCap = "butt";
                n.lineJoin = "miter";
                n.miterLimit = 10;
                n.globalCompositeOperation = "source-over";
                n.font = "10px sans-serif";
                n.setLineDash !== undefined && (n.setLineDash([]), n.lineDashOffset = 0)
            }
            function ut(n, t, i, r) {
                for (var f, e, o = n.length,
                u = 3; u < o; u += 4) f = n[u],
                f === 0 ? (n[u - 3] = t, n[u - 2] = i, n[u - 1] = r) : f < 255 && (e = 255 - f, n[u - 3] = n[u - 3] * f + t * e >> 8, n[u - 2] = n[u - 2] * f + i * e >> 8, n[u - 1] = n[u - 1] * f + r * e >> 8)
            }
            function ft(n, t, i) {
                for (var u, f = n.length,
                r = 3; r < f; r += 4) u = i ? i[n[r]] : n[r],
                t[r] = t[r] * u * (1 / 255) | 0
            }
            function et(n, t, i) {
                for (var u, f = n.length,
                r = 3; r < f; r += 4) u = n[r - 3] * 77 + n[r - 2] * 152 + n[r - 1] * 28,
                t[r] = i ? t[r] * i[u >> 8] >> 8 : t[r] * u >> 16
            }
            function ot(n, t, i, r, u, f, e) {
                var s = !!f,
                p = s ? f[0] : 0,
                w = s ? f[1] : 0,
                b = s ? f[2] : 0,
                c,
                l,
                h,
                o;
                for (c = u === "Luminosity" ? et: ft, l = 1048576, h = Math.min(r, Math.ceil(l / i)), o = 0; o < r; o += h) {
                    var a = Math.min(h, r - o),
                    v = n.getImageData(0, o, i, a),
                    y = t.getImageData(0, o, i, a);
                    s && ut(v.data, p, w, b);
                    c(v.data, y.data, e);
                    n.putImageData(y, 0, o)
                }
            }
            function tt(n, t, i) {
                var r = t.canvas,
                e = t.context,
                u, f;
                if (n.setTransform(t.scaleX, 0, 0, t.scaleY, t.offsetX, t.offsetY), u = t.backdrop || null, !t.transferMap && o.WebGLUtils.isEnabled) {
                    f = o.WebGLUtils.composeSMask(i.canvas, r, {
                        subtype: t.subtype,
                        backdrop: u
                    });
                    n.setTransform(1, 0, 0, 1, 0, 0);
                    n.drawImage(f, t.offsetX, t.offsetY);
                    return
                }
                ot(e, i, r.width, r.height, t.subtype, u, t.transferMap);
                n.drawImage(r, 0, 0)
            }
            var rt = 15,
            d = 10,
            st = ["butt", "round", "square"],
            ht = ["miter", "round", "bevel"],
            ct = {},
            it = {},
            w;
            n.prototype = {
                beginDrawing: function(n) {
                    var i = n.transform,
                    e = n.viewport,
                    o = n.transparency,
                    r = n.background,
                    s = r === undefined ? null: r,
                    u = this.ctx.canvas.width,
                    f = this.ctx.canvas.height,
                    t;
                    this.ctx.save();
                    this.ctx.fillStyle = s || "rgb(255, 255, 255)";
                    this.ctx.fillRect(0, 0, u, f);
                    this.ctx.restore();
                    o && (t = this.cachedCanvases.getCanvas("transparent", u, f, !0), this.compositeCtx = this.ctx, this.transparentCanvas = t.canvas, this.ctx = t.context, this.ctx.save(), this.ctx.transform.apply(this.ctx, this.compositeCtx.mozCurrentTransform));
                    this.ctx.save();
                    nt(this.ctx);
                    i && this.ctx.transform.apply(this.ctx, i);
                    this.ctx.transform.apply(this.ctx, e.transform);
                    this.baseTransform = this.ctx.mozCurrentTransform.slice();
                    this.imageLayer && this.imageLayer.beginLayout()
                },
                executeOperatorList: function(n, t, i, u) {
                    var s = n.argsArray,
                    w = n.fnArray,
                    f = t || 0,
                    h = s.length,
                    l, e, y;
                    if (h === f) return f;
                    for (var a = h - f > d && typeof i == "function",
                    b = a ? Date.now() + rt: 0, v = 0, k = this.commonObjs, g = this.objs, c;;) {
                        if (u !== undefined && f === u.nextBreakPoint) return u.breakIt(f, i),
                        f;
                        if (c = w[f], c !== r.OPS.dependency) this[c].apply(this, s[f]);
                        else for (l = s[f], e = 0, y = l.length; e < y; e++) {
                            var o = l[e],
                            nt = o[0] === "g" && o[1] === "_",
                            p = nt ? k: g;
                            if (!p.isResolved(o)) return p.get(o, i),
                            f
                        }
                        if (f++, f === h) return f;
                        if (a && ++v > d) {
                            if (Date.now() > b) return i(),
                            f;
                            v = 0
                        }
                    }
                },
                endDrawing: function() {
                    this.current.activeSMask !== null && this.endSMaskGroup();
                    this.ctx.restore();
                    this.transparentCanvas && (this.ctx = this.compositeCtx, this.ctx.save(), this.ctx.setTransform(1, 0, 0, 1, 0, 0), this.ctx.drawImage(this.transparentCanvas, 0, 0), this.ctx.restore(), this.transparentCanvas = null);
                    this.cachedCanvases.clear();
                    o.WebGLUtils.clear();
                    this.imageLayer && this.imageLayer.endLayout()
                },
                setLineWidth: function(n) {
                    this.current.lineWidth = n;
                    this.ctx.lineWidth = n
                },
                setLineCap: function(n) {
                    this.ctx.lineCap = st[n]
                },
                setLineJoin: function(n) {
                    this.ctx.lineJoin = ht[n]
                },
                setMiterLimit: function(n) {
                    this.ctx.miterLimit = n
                },
                setDash: function(n, t) {
                    var i = this.ctx;
                    i.setLineDash !== undefined && (i.setLineDash(n), i.lineDashOffset = t)
                },
                setRenderingIntent: function() {},
                setFlatness: function() {},
                setGState: function(n) {
                    for (var r = 0,
                    u = n.length; r < u; r++) {
                        var i = n[r],
                        f = i[0],
                        t = i[1];
                        switch (f) {
                        case "LW":
                            this.setLineWidth(t);
                            break;
                        case "LC":
                            this.setLineCap(t);
                            break;
                        case "LJ":
                            this.setLineJoin(t);
                            break;
                        case "ML":
                            this.setMiterLimit(t);
                            break;
                        case "D":
                            this.setDash(t[0], t[1]);
                            break;
                        case "RI":
                            this.setRenderingIntent(t);
                            break;
                        case "FL":
                            this.setFlatness(t);
                            break;
                        case "Font":
                            this.setFont(t[0], t[1]);
                            break;
                        case "CA":
                            this.current.strokeAlpha = i[1];
                            break;
                        case "ca":
                            this.current.fillAlpha = i[1];
                            this.ctx.globalAlpha = i[1];
                            break;
                        case "BM":
                            this.ctx.globalCompositeOperation = t;
                            break;
                        case "SMask":
                            this.current.activeSMask && (this.stateStack.length > 0 && this.stateStack[this.stateStack.length - 1].activeSMask === this.current.activeSMask ? this.suspendSMaskGroup() : this.endSMaskGroup());
                            this.current.activeSMask = t ? this.tempSMask: null;
                            this.current.activeSMask && this.beginSMaskGroup();
                            this.tempSMask = null
                        }
                    }
                },
                beginSMaskGroup: function() {
                    var i = this.current.activeSMask,
                    u = i.canvas.width,
                    f = i.canvas.height,
                    e = "smaskGroupAt" + this.groupLevel,
                    o = this.cachedCanvases.getCanvas(e, u, f, !0),
                    r = this.ctx,
                    s = r.mozCurrentTransform,
                    n;
                    this.ctx.save();
                    n = o.context;
                    n.scale(1 / i.scaleX, 1 / i.scaleY);
                    n.translate( - i.offsetX, -i.offsetY);
                    n.transform.apply(n, s);
                    i.startTransformInverse = n.mozCurrentTransformInverse;
                    t(r, n);
                    this.ctx = n;
                    this.setGState([["BM", "source-over"], ["ca", 1], ["CA", 1]]);
                    this.groupStack.push(r);
                    this.groupLevel++
                },
                suspendSMaskGroup: function() {
                    var n = this.ctx,
                    i;
                    this.groupLevel--;
                    this.ctx = this.groupStack.pop();
                    tt(this.ctx, this.current.activeSMask, n);
                    this.ctx.restore();
                    this.ctx.save();
                    t(n, this.ctx);
                    this.current.resumeSMaskCtx = n;
                    i = r.Util.transform(this.current.activeSMask.startTransformInverse, n.mozCurrentTransform);
                    this.ctx.transform.apply(this.ctx, i);
                    n.save();
                    n.setTransform(1, 0, 0, 1, 0, 0);
                    n.clearRect(0, 0, n.canvas.width, n.canvas.height);
                    n.restore()
                },
                resumeSMaskGroup: function() {
                    var n = this.current.resumeSMaskCtx,
                    t = this.ctx;
                    this.ctx = n;
                    this.groupStack.push(t);
                    this.groupLevel++
                },
                endSMaskGroup: function() {
                    var n = this.ctx,
                    i;
                    this.groupLevel--;
                    this.ctx = this.groupStack.pop();
                    tt(this.ctx, this.current.activeSMask, n);
                    this.ctx.restore();
                    t(n, this.ctx);
                    i = r.Util.transform(this.current.activeSMask.startTransformInverse, n.mozCurrentTransform);
                    this.ctx.transform.apply(this.ctx, i)
                },
                save: function() {
                    this.ctx.save();
                    var n = this.current;
                    this.stateStack.push(n);
                    this.current = n.clone();
                    this.current.resumeSMaskCtx = null
                },
                restore: function() {
                    this.current.resumeSMaskCtx && this.resumeSMaskGroup();
                    this.current.activeSMask !== null && (this.stateStack.length === 0 || this.stateStack[this.stateStack.length - 1].activeSMask !== this.current.activeSMask) && this.endSMaskGroup();
                    this.stateStack.length !== 0 && (this.current = this.stateStack.pop(), this.ctx.restore(), this.pendingClip = null, this.cachedGetSinglePixelWidth = null)
                },
                transform: function(n, t, i, r, u, f) {
                    this.ctx.transform(n, t, i, r, u, f);
                    this.cachedGetSinglePixelWidth = null
                },
                constructPath: function(n, t) {
                    for (var o, s, l, a, e = this.ctx,
                    h = this.current,
                    u = h.x,
                    f = h.y,
                    c = 0,
                    i = 0,
                    v = n.length; c < v; c++) switch (n[c] | 0) {
                    case r.OPS.rectangle:
                        u = t[i++];
                        f = t[i++];
                        o = t[i++];
                        s = t[i++];
                        o === 0 && (o = this.getSinglePixelWidth());
                        s === 0 && (s = this.getSinglePixelWidth());
                        l = u + o;
                        a = f + s;
                        this.ctx.moveTo(u, f);
                        this.ctx.lineTo(l, f);
                        this.ctx.lineTo(l, a);
                        this.ctx.lineTo(u, a);
                        this.ctx.lineTo(u, f);
                        this.ctx.closePath();
                        break;
                    case r.OPS.moveTo:
                        u = t[i++];
                        f = t[i++];
                        e.moveTo(u, f);
                        break;
                    case r.OPS.lineTo:
                        u = t[i++];
                        f = t[i++];
                        e.lineTo(u, f);
                        break;
                    case r.OPS.curveTo:
                        u = t[i + 4];
                        f = t[i + 5];
                        e.bezierCurveTo(t[i], t[i + 1], t[i + 2], t[i + 3], u, f);
                        i += 6;
                        break;
                    case r.OPS.curveTo2:
                        e.bezierCurveTo(u, f, t[i], t[i + 1], t[i + 2], t[i + 3]);
                        u = t[i + 2];
                        f = t[i + 3];
                        i += 4;
                        break;
                    case r.OPS.curveTo3:
                        u = t[i + 2];
                        f = t[i + 3];
                        e.bezierCurveTo(t[i], t[i + 1], u, f, u, f);
                        i += 4;
                        break;
                    case r.OPS.closePath:
                        e.closePath()
                    }
                    h.setCurrentPoint(u, f)
                },
                closePath: function() {
                    this.ctx.closePath()
                },
                stroke: function(n) {
                    n = typeof n != "undefined" ? n: !0;
                    var t = this.ctx,
                    i = this.current.strokeColor;
                    t.lineWidth = Math.max(this.getSinglePixelWidth() * l, this.current.lineWidth);
                    t.globalAlpha = this.current.strokeAlpha;
                    i && i.hasOwnProperty("type") && i.type === "Pattern" ? (t.save(), t.strokeStyle = i.getPattern(t, this), t.stroke(), t.restore()) : t.stroke();
                    n && this.consumePath();
                    t.globalAlpha = this.current.fillAlpha
                },
                closeStroke: function() {
                    this.closePath();
                    this.stroke()
                },
                fill: function(n) {
                    n = typeof n != "undefined" ? n: !0;
                    var t = this.ctx,
                    r = this.current.fillColor,
                    u = this.current.patternFill,
                    i = !1;
                    u && (t.save(), this.baseTransform && t.setTransform.apply(t, this.baseTransform), t.fillStyle = r.getPattern(t, this), i = !0);
                    this.pendingEOFill ? (t.fill("evenodd"), this.pendingEOFill = !1) : t.fill();
                    i && t.restore();
                    n && this.consumePath()
                },
                eoFill: function() {
                    this.pendingEOFill = !0;
                    this.fill()
                },
                fillStroke: function() {
                    this.fill(!1);
                    this.stroke(!1);
                    this.consumePath()
                },
                eoFillStroke: function() {
                    this.pendingEOFill = !0;
                    this.fillStroke()
                },
                closeFillStroke: function() {
                    this.closePath();
                    this.fillStroke()
                },
                closeEOFillStroke: function() {
                    this.pendingEOFill = !0;
                    this.closePath();
                    this.fillStroke()
                },
                endPath: function() {
                    this.consumePath()
                },
                clip: function() {
                    this.pendingClip = ct
                },
                eoClip: function() {
                    this.pendingClip = it
                },
                beginText: function() {
                    this.current.textMatrix = r.IDENTITY_MATRIX;
                    this.current.textMatrixScale = 1;
                    this.current.x = this.current.lineX = 0;
                    this.current.y = this.current.lineY = 0
                },
                endText: function() {
                    var r = this.pendingTextPaths,
                    n = this.ctx,
                    i, t;
                    if (r === undefined) {
                        n.beginPath();
                        return
                    }
                    for (n.save(), n.beginPath(), i = 0; i < r.length; i++) t = r[i],
                    n.setTransform.apply(n, t.transform),
                    n.translate(t.x, t.y),
                    t.addToPath(n, t.fontSize);
                    n.restore();
                    n.clip();
                    n.beginPath();
                    delete this.pendingTextPaths
                },
                setCharSpacing: function(n) {
                    this.current.charSpacing = n
                },
                setWordSpacing: function(n) {
                    this.current.wordSpacing = n
                },
                setHScale: function(n) {
                    this.current.textHScale = n / 100
                },
                setLeading: function(n) {
                    this.current.leading = -n
                },
                setFont: function(n, t) {
                    var i = this.commonObjs.get(n),
                    u = this.current,
                    e;
                    if (!i) throw new Error("Can't find font for " + n);
                    if (u.fontMatrix = i.fontMatrix ? i.fontMatrix: r.FONT_IDENTITY_MATRIX, (u.fontMatrix[0] === 0 || u.fontMatrix[3] === 0) && r.warn("Invalid font matrix for font " + n), t < 0 ? (t = -t, u.fontDirection = -1) : u.fontDirection = 1, this.current.font = i, this.current.fontSize = t, !i.isType3Font) {
                        var o = i.loadedName || "sans-serif",
                        s = i.black ? "900": i.bold ? "bold": "normal",
                        l = i.italic ? "italic": "normal",
                        a = '"' + o + '", ' + i.fallbackName,
                        f = t < h ? h: t > c ? c: t;
                        this.current.fontSizeScale = t / f;
                        e = l + " " + s + " " + f + "px " + a;
                        this.ctx.font = e
                    }
                },
                setTextRenderingMode: function(n) {
                    this.current.textRenderingMode = n
                },
                setTextRise: function(n) {
                    this.current.textRise = n
                },
                moveText: function(n, t) {
                    this.current.x = this.current.lineX += n;
                    this.current.y = this.current.lineY += t
                },
                setLeadingMoveText: function(n, t) {
                    this.setLeading( - t);
                    this.moveText(n, t)
                },
                setTextMatrix: function(n, t, i, r, u, f) {
                    this.current.textMatrix = [n, t, i, r, u, f];
                    this.current.textMatrixScale = Math.sqrt(n * n + t * t);
                    this.current.x = this.current.lineX = 0;
                    this.current.y = this.current.lineY = 0
                },
                nextLine: function() {
                    this.moveText(0, this.current.leading)
                },
                paintChar: function(n, t, i) {
                    var u = this.ctx,
                    e = this.current,
                    o = e.font,
                    h = e.textRenderingMode,
                    c = e.fontSize / e.fontSizeScale,
                    f = h & r.TextRenderingMode.FILL_STROKE_MASK,
                    l = !!(h & r.TextRenderingMode.ADD_TO_PATH_FLAG),
                    s,
                    a; (o.disableFontFace || l) && (s = o.getPathGenerator(this.commonObjs, n));
                    o.disableFontFace ? (u.save(), u.translate(t, i), u.beginPath(), s(u, c), (f === r.TextRenderingMode.FILL || f === r.TextRenderingMode.FILL_STROKE) && u.fill(), (f === r.TextRenderingMode.STROKE || f === r.TextRenderingMode.FILL_STROKE) && u.stroke(), u.restore()) : ((f === r.TextRenderingMode.FILL || f === r.TextRenderingMode.FILL_STROKE) && u.fillText(n, t, i), (f === r.TextRenderingMode.STROKE || f === r.TextRenderingMode.FILL_STROKE) && u.strokeText(n, t, i));
                    l && (a = this.pendingTextPaths || (this.pendingTextPaths = []), a.push({
                        transform: u.mozCurrentTransform,
                        x: t,
                        y: i,
                        fontSize: c,
                        addToPath: s
                    }))
                },
                get isFontSubpixelAAEnabled() {
                    var i = this.canvasFactory.create(10, 10).context,
                    t,
                    u,
                    n;
                    for (i.scale(1.5, 1), i.fillText("I", 0, 10), t = i.getImageData(0, 0, 10, 10).data, u = !1, n = 3; n < t.length; n += 4) if (t[n] > 0 && t[n] < 255) {
                        u = !0;
                        break
                    }
                    return r.shadow(this, "isFontSubpixelAAEnabled", u)
                },
                showText: function(n) {
                    var t = this.current,
                    h = t.font,
                    c, a, it, rt, o, k, f, p, d, ht, w, ft, ct;
                    if (h.isType3Font) return this.showType3Text(n);
                    if (c = t.fontSize, c !== 0) {
                        var i = this.ctx,
                        u = t.fontSizeScale,
                        lt = t.charSpacing,
                        at = t.wordSpacing,
                        g = t.fontDirection,
                        b = t.textHScale * g,
                        vt = n.length,
                        nt = h.vertical,
                        yt = nt ? 1 : -1,
                        pt = h.defaultVMetrics,
                        tt = c * t.fontMatrix[0],
                        wt = t.textRenderingMode === r.TextRenderingMode.FILL && !h.disableFontFace;
                        for (i.save(), i.transform.apply(i, t.textMatrix), i.translate(t.x, t.y + t.textRise), t.patternFill && (i.fillStyle = t.fillColor.getPattern(i, this)), g > 0 ? i.scale(b, -1) : i.scale(b, 1), a = t.lineWidth, it = t.textMatrixScale, it === 0 || a === 0 ? (rt = t.textRenderingMode & r.TextRenderingMode.FILL_STROKE_MASK, (rt === r.TextRenderingMode.STROKE || rt === r.TextRenderingMode.FILL_STROKE) && (this.cachedGetSinglePixelWidth = null, a = this.getSinglePixelWidth() * l)) : a /= it, u !== 1 && (i.scale(u, u), a /= u), i.lineWidth = a, o = 0, k = 0; k < vt; ++k) {
                            if (f = n[k], r.isNum(f)) {
                                o += yt * f * c / 1e3;
                                continue
                            }
                            var et = !1,
                            bt = (f.isSpace ? at: 0) + lt,
                            ut = f.fontChar,
                            v = f.accent,
                            s,
                            y,
                            ot,
                            st,
                            e = f.width;
                            nt ? (p = f.vmetric || pt, d = f.vmetric ? p[1] : e * .5, d = -d * tt, ht = p[2] * tt, e = p ? -p[0] : e, s = d / u, y = (o + ht) / u) : (s = o / u, y = 0);
                            h.remeasure && e > 0 && (w = i.measureText(ut).width * 1e3 / c * u, e < w && this.isFontSubpixelAAEnabled ? (ft = e / w, et = !0, i.save(), i.scale(ft, 1), s /= ft) : e !== w && (s += (e - w) / 2e3 * c / u)); (f.isInFont || h.missingFile) && (wt && !v ? i.fillText(ut, s, y) : (this.paintChar(ut, s, y), v && (ot = s + v.offset.x / u, st = y - v.offset.y / u, this.paintChar(v.fontChar, ot, st))));
                            ct = e * tt + bt * g;
                            o += ct;
                            et && i.restore()
                        }
                        nt ? t.y -= o * b: t.x += o * b;
                        i.restore()
                    }
                },
                showType3Text: function(n) {
                    var i = this.ctx,
                    t = this.current,
                    l = t.font,
                    f = t.fontSize,
                    a = t.fontDirection,
                    w = l.vertical ? 1 : -1,
                    b = t.charSpacing,
                    k = t.wordSpacing,
                    o = t.textHScale * a,
                    v = t.fontMatrix || r.FONT_IDENTITY_MATRIX,
                    d = n.length,
                    g = t.textRenderingMode === r.TextRenderingMode.INVISIBLE,
                    e,
                    u,
                    s,
                    h,
                    y,
                    c,
                    p;
                    if (!g && f !== 0) {
                        for (this.cachedGetSinglePixelWidth = null, i.save(), i.transform.apply(i, t.textMatrix), i.translate(t.x, t.y), i.scale(o, a), e = 0; e < d; ++e) {
                            if (u = n[e], r.isNum(u)) {
                                h = w * u * f / 1e3;
                                this.ctx.translate(h, 0);
                                t.x += h * o;
                                continue
                            }
                            if (y = (u.isSpace ? k: 0) + b, c = l.charProcOperatorList[u.operatorListId], !c) {
                                r.warn('Type3 character "' + u.operatorListId + '" is not available.');
                                continue
                            }
                            this.processingType3 = u;
                            this.save();
                            i.scale(f, f);
                            i.transform.apply(i, v);
                            this.executeOperatorList(c);
                            this.restore();
                            p = r.Util.applyTransform([u.width, 0], v);
                            s = p[0] * f + y;
                            i.translate(s, 0);
                            t.x += s * o
                        }
                        i.restore();
                        this.processingType3 = null
                    }
                },
                setCharWidth: function() {},
                setCharWidthAndBounds: function(n, t, i, r, u, f) {
                    this.ctx.rect(i, r, u - i, f - r);
                    this.clip();
                    this.endPath()
                },
                getColorN_Pattern: function(t) {
                    var i = this,
                    r;
                    if (t[0] === "TilingPattern") {
                        var u = t[1],
                        f = this.baseTransform || this.ctx.mozCurrentTransform.slice(),
                        o = {
                            createCanvasGraphics: function(t) {
                                return new n(t, i.commonObjs, i.objs, i.canvasFactory)
                            }
                        };
                        r = new e.TilingPattern(t, u, this.ctx, o, f)
                    } else r = e.getShadingPatternFromIR(t);
                    return r
                },
                setStrokeColorN: function() {
                    this.current.strokeColor = this.getColorN_Pattern(arguments)
                },
                setFillColorN: function() {
                    this.current.fillColor = this.getColorN_Pattern(arguments);
                    this.current.patternFill = !0
                },
                setStrokeRGBColor: function(n, t, i) {
                    var u = r.Util.makeCssRgb(n, t, i);
                    this.ctx.strokeStyle = u;
                    this.current.strokeColor = u
                },
                setFillRGBColor: function(n, t, i) {
                    var u = r.Util.makeCssRgb(n, t, i);
                    this.ctx.fillStyle = u;
                    this.current.fillColor = u;
                    this.current.patternFill = !1
                },
                shadingFill: function(n) {
                    var i = this.ctx,
                    h, t;
                    if (this.save(), h = e.getShadingPatternFromIR(n), i.fillStyle = h.getPattern(i, this, !0), t = i.mozCurrentTransformInverse, t) {
                        var c = i.canvas,
                        l = c.width,
                        a = c.height,
                        u = r.Util.applyTransform([0, 0], t),
                        f = r.Util.applyTransform([0, a], t),
                        o = r.Util.applyTransform([l, 0], t),
                        s = r.Util.applyTransform([l, a], t),
                        v = Math.min(u[0], f[0], o[0], s[0]),
                        y = Math.min(u[1], f[1], o[1], s[1]),
                        p = Math.max(u[0], f[0], o[0], s[0]),
                        w = Math.max(u[1], f[1], o[1], s[1]);
                        this.ctx.fillRect(v, y, p - v, w - y)
                    } else this.ctx.fillRect( - 1e10, -1e10, 2e10, 2e10);
                    this.restore()
                },
                beginInlineImage: function() {
                    throw new Error("Should not call beginInlineImage");
                },
                beginImageData: function() {
                    throw new Error("Should not call beginImageData");
                },
                paintFormXObjectBegin: function(n, t) {
                    if (this.save(), this.baseTransformStack.push(this.baseTransform), Array.isArray(n) && n.length === 6 && this.transform.apply(this, n), this.baseTransform = this.ctx.mozCurrentTransform, Array.isArray(t) && t.length === 4) {
                        var i = t[2] - t[0],
                        r = t[3] - t[1];
                        this.ctx.rect(t[0], t[1], i, r);
                        this.clip();
                        this.endPath()
                    }
                },
                paintFormXObjectEnd: function() {
                    this.restore();
                    this.baseTransform = this.baseTransformStack.pop()
                },
                beginGroup: function(n) {
                    var i, p, e, w, v, y, u;
                    if (this.save(), i = this.ctx, n.isolated || r.info("TODO: Support non-isolated groups."), n.knockout && r.warn("Knockout groups not supported."), p = i.mozCurrentTransform, n.matrix && i.transform.apply(i, n.matrix), !n.bbox) throw new Error("Bounding box is required.");
                    e = r.Util.getAxialAlignedBoundingBox(n.bbox, i.mozCurrentTransform);
                    w = [0, 0, i.canvas.width, i.canvas.height];
                    e = r.Util.intersect(e, w) || [0, 0, 0, 0];
                    var o = Math.floor(e[0]),
                    s = Math.floor(e[1]),
                    h = Math.max(Math.ceil(e[2]) - o, 1),
                    c = Math.max(Math.ceil(e[3]) - s, 1),
                    l = 1,
                    a = 1;
                    h > f && (l = h / f, h = f);
                    c > f && (a = c / f, c = f);
                    v = "groupAt" + this.groupLevel;
                    n.smask && (v += "_smask_" + this.smaskCounter++%2);
                    y = this.cachedCanvases.getCanvas(v, h, c, !0);
                    u = y.context;
                    u.scale(1 / l, 1 / a);
                    u.translate( - o, -s);
                    u.transform.apply(u, p);
                    n.smask ? this.smaskStack.push({
                        canvas: y.canvas,
                        context: u,
                        offsetX: o,
                        offsetY: s,
                        scaleX: l,
                        scaleY: a,
                        subtype: n.smask.subtype,
                        backdrop: n.smask.backdrop,
                        transferMap: n.smask.transferMap || null,
                        startTransformInverse: null
                    }) : (i.setTransform(1, 0, 0, 1, 0, 0), i.translate(o, s), i.scale(l, a));
                    t(i, u);
                    this.ctx = u;
                    this.setGState([["BM", "source-over"], ["ca", 1], ["CA", 1]]);
                    this.groupStack.push(i);
                    this.groupLevel++;
                    this.current.activeSMask = null
                },
                endGroup: function(n) {
                    this.groupLevel--;
                    var t = this.ctx;
                    this.ctx = this.groupStack.pop();
                    this.ctx.imageSmoothingEnabled !== undefined ? this.ctx.imageSmoothingEnabled = !1 : this.ctx.mozImageSmoothingEnabled = !1;
                    n.smask ? this.tempSMask = this.smaskStack.pop() : this.ctx.drawImage(t.canvas, 0, 0);
                    this.restore()
                },
                beginAnnotations: function() {
                    this.save();
                    this.baseTransform && this.ctx.setTransform.apply(this.ctx, this.baseTransform)
                },
                endAnnotations: function() {
                    this.restore()
                },
                beginAnnotation: function(n, t, i) {
                    if (this.save(), nt(this.ctx), this.current = new s, Array.isArray(n) && n.length === 4) {
                        var r = n[2] - n[0],
                        u = n[3] - n[1];
                        this.ctx.rect(n[0], n[1], r, u);
                        this.clip();
                        this.endPath()
                    }
                    this.transform.apply(this, t);
                    this.transform.apply(this, i)
                },
                endAnnotation: function() {
                    this.restore()
                },
                paintJpegXObject: function(n, t, i) {
                    var u = this.objs.get(n),
                    f,
                    e,
                    o;
                    if (!u) {
                        r.warn("Dependent image isn't ready yet");
                        return
                    }
                    this.save();
                    f = this.ctx;
                    f.scale(1 / t, -1 / i);
                    f.drawImage(u, 0, 0, u.width, u.height, 0, -i, t, i);
                    this.imageLayer && (e = f.mozCurrentTransformInverse, o = this.getCanvasPosition(0, 0), this.imageLayer.appendImage({
                        objId: n,
                        left: o[0],
                        top: o[1],
                        width: t / e[0],
                        height: i / e[3]
                    }));
                    this.restore()
                },
                paintImageMaskXObject: function(n) {
                    var s = this.ctx,
                    u = n.width,
                    f = n.height,
                    o = this.current.fillColor,
                    h = this.current.patternFill,
                    r = this.processingType3,
                    e, t;
                    if (b && r && r.compiled === undefined && (r.compiled = u <= a && f <= a ? k({
                        data: n.data,
                        width: u,
                        height: f
                    }) : null), r && r.compiled) {
                        r.compiled(s);
                        return
                    }
                    e = this.cachedCanvases.getCanvas("maskCanvas", u, f);
                    t = e.context;
                    t.save();
                    i(t, n);
                    t.globalCompositeOperation = "source-in";
                    t.fillStyle = h ? o.getPattern(t, this) : o;
                    t.fillRect(0, 0, u, f);
                    t.restore();
                    this.paintInlineImageXObject(e.canvas)
                },
                paintImageMaskXObjectRepeat: function(n, t, r, u) {
                    var s = n.width,
                    h = n.height,
                    c = this.current.fillColor,
                    v = this.current.patternFill,
                    l = this.cachedCanvases.getCanvas("maskCanvas", s, h),
                    f = l.context,
                    e,
                    o,
                    a;
                    for (f.save(), i(f, n), f.globalCompositeOperation = "source-in", f.fillStyle = v ? c.getPattern(f, this) : c, f.fillRect(0, 0, s, h), f.restore(), e = this.ctx, o = 0, a = u.length; o < a; o += 2) e.save(),
                    e.transform(t, 0, 0, r, u[o], u[o + 1]),
                    e.scale(1, -1),
                    e.drawImage(l.canvas, 0, 0, s, h, 0, -1, 1, 1),
                    e.restore()
                },
                paintImageMaskXObjectGroup: function(n) {
                    for (var r = this.ctx,
                    s = this.current.fillColor,
                    c = this.current.patternFill,
                    f = 0,
                    l = n.length; f < l; f++) {
                        var u = n[f],
                        e = u.width,
                        o = u.height,
                        h = this.cachedCanvases.getCanvas("maskCanvas", e, o),
                        t = h.context;
                        t.save();
                        i(t, u);
                        t.globalCompositeOperation = "source-in";
                        t.fillStyle = c ? s.getPattern(t, this) : s;
                        t.fillRect(0, 0, e, o);
                        t.restore();
                        r.save();
                        r.transform.apply(r, u.transform);
                        r.scale(1, -1);
                        r.drawImage(h.canvas, 0, 0, e, o, 0, -1, 1, 1);
                        r.restore()
                    }
                },
                paintImageXObject: function(n) {
                    var t = this.objs.get(n);
                    if (!t) {
                        r.warn("Dependent image isn't ready yet");
                        return
                    }
                    this.paintInlineImageXObject(t)
                },
                paintImageXObjectRepeat: function(n, t, i, u) {
                    var e = this.objs.get(n),
                    f,
                    s;
                    if (!e) {
                        r.warn("Dependent image isn't ready yet");
                        return
                    }
                    var h = e.width,
                    c = e.height,
                    o = [];
                    for (f = 0, s = u.length; f < s; f += 2) o.push({
                        transform: [t, 0, 0, i, u[f], u[f + 1]],
                        x: 0,
                        y: 0,
                        w: h,
                        h: c
                    });
                    this.paintInlineImageXObjectGroup(e, o)
                },
                paintInlineImageXObject: function(n) {
                    var s = n.width,
                    r = n.height,
                    l = this.ctx,
                    c, u, f, p;
                    this.save();
                    l.scale(1 / s, -1 / r);
                    var e = l.mozCurrentTransformInverse,
                    w = e[0],
                    b = e[1],
                    a = Math.max(Math.sqrt(w * w + b * b), 1),
                    k = e[2],
                    d = e[3],
                    v = Math.max(Math.sqrt(k * k + d * d), 1),
                    h,
                    o;
                    n instanceof HTMLElement || !n.data ? h = n: (o = this.cachedCanvases.getCanvas("inlineImage", s, r), c = o.context, g(c, n), h = o.canvas);
                    for (var t = s,
                    i = r,
                    y = "prescale1"; a > 2 && t > 1 || v > 2 && i > 1;) u = t,
                    f = i,
                    a > 2 && t > 1 && (u = Math.ceil(t / 2), a /= t / u),
                    v > 2 && i > 1 && (f = Math.ceil(i / 2), v /= i / f),
                    o = this.cachedCanvases.getCanvas(y, u, f),
                    c = o.context,
                    c.clearRect(0, 0, u, f),
                    c.drawImage(h, 0, 0, t, i, 0, 0, u, f),
                    h = o.canvas,
                    t = u,
                    i = f,
                    y = y === "prescale1" ? "prescale2": "prescale1";
                    l.drawImage(h, 0, 0, t, i, 0, -r, s, r);
                    this.imageLayer && (p = this.getCanvasPosition(0, -r), this.imageLayer.appendImage({
                        imgData: n,
                        left: p[0],
                        top: p[1],
                        width: s / e[0],
                        height: r / e[3]
                    }));
                    this.restore()
                },
                paintInlineImageXObjectGroup: function(n, t) {
                    var r = this.ctx,
                    e = n.width,
                    o = n.height,
                    s = this.cachedCanvases.getCanvas("inlineImage", e, o),
                    c = s.context,
                    u,
                    h,
                    i,
                    f;
                    for (g(c, n), u = 0, h = t.length; u < h; u++) i = t[u],
                    r.save(),
                    r.transform.apply(r, i.transform),
                    r.scale(1, -1),
                    r.drawImage(s.canvas, i.x, i.y, i.w, i.h, 0, -1, 1, 1),
                    this.imageLayer && (f = this.getCanvasPosition(i.x, i.y), this.imageLayer.appendImage({
                        imgData: n,
                        left: f[0],
                        top: f[1],
                        width: e,
                        height: o
                    })),
                    r.restore()
                },
                paintSolidColorImageMask: function() {
                    this.ctx.fillRect(0, 0, 1, 1)
                },
                paintXObject: function() {
                    r.warn("Unsupported 'paintXObject' command.")
                },
                markPoint: function() {},
                markPointProps: function() {},
                beginMarkedContent: function() {},
                beginMarkedContentProps: function() {},
                endMarkedContent: function() {},
                beginCompat: function() {},
                endCompat: function() {},
                consumePath: function() {
                    var n = this.ctx;
                    this.pendingClip && (this.pendingClip === it ? n.clip("evenodd") : n.clip(), this.pendingClip = null);
                    n.beginPath()
                },
                getSinglePixelWidth: function() {
                    if (this.cachedGetSinglePixelWidth === null) {
                        this.ctx.save();
                        var n = this.ctx.mozCurrentTransformInverse;
                        this.ctx.restore();
                        this.cachedGetSinglePixelWidth = Math.sqrt(Math.max(n[0] * n[0] + n[1] * n[1], n[2] * n[2] + n[3] * n[3]))
                    }
                    return this.cachedGetSinglePixelWidth
                },
                getCanvasPosition: function(n, t) {
                    var i = this.ctx.mozCurrentTransform;
                    return [i[0] * n + i[2] * t + i[4], i[1] * n + i[3] * t + i[5]]
                }
            };
            for (w in r.OPS) n.prototype[r.OPS[w]] = n.prototype[w];
            return n
        } ();
        t.CanvasGraphics = w
    },
    function(n, t, i) {
        "use strict";
        function s(n) {
            var t = u[n[0]];
            if (!t) throw new Error("Unknown IR type: " + n[0]);
            return t.fromIR(n)
        }
        var e, o;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.TilingPattern = t.getShadingPatternFromIR = undefined;
        var r = i(0),
        f = i(58),
        u = {};
        u.RadialAxial = {
            fromIR: function(n) {
                var r = n[1],
                u = n[2],
                t = n[3],
                i = n[4],
                f = n[5],
                e = n[6];
                return {
                    type: "Pattern",
                    getPattern: function(n) {
                        var o, s, c, h;
                        for (r === "axial" ? o = n.createLinearGradient(t[0], t[1], i[0], i[1]) : r === "radial" && (o = n.createRadialGradient(t[0], t[1], f, i[0], i[1], e)), s = 0, c = u.length; s < c; ++s) h = u[s],
                        o.addColorStop(h[0], h[1]);
                        return o
                    }
                }
            }
        };
        e = function() {
            function n(n, t, i, r, u, f, e, o) {
                var h = t.coords,
                a = t.colors,
                b = n.data,
                dt = n.width * 4,
                c, l, et;
                h[i + 1] > h[r + 1] && (c = i, i = r, r = c, c = f, f = e, e = c);
                h[r + 1] > h[u + 1] && (c = r, r = u, u = c, c = e, e = o, o = c);
                h[i + 1] > h[r + 1] && (c = i, i = r, r = c, c = f, f = e, e = c);
                var k = (h[i] + t.offsetX) * t.scaleX,
                v = (h[i + 1] + t.offsetY) * t.scaleY,
                ot = (h[r] + t.offsetX) * t.scaleX,
                p = (h[r + 1] + t.offsetY) * t.scaleY,
                lt = (h[u] + t.offsetX) * t.scaleX,
                y = (h[u + 1] + t.offsetY) * t.scaleY;
                if (! (v >= y)) {
                    var d = a[f],
                    g = a[f + 1],
                    nt = a[f + 2],
                    st = a[e],
                    ht = a[e + 1],
                    ct = a[e + 2],
                    at = a[o],
                    vt = a[o + 1],
                    yt = a[o + 2],
                    gt = Math.round(v),
                    ni = Math.round(y),
                    w,
                    tt,
                    it,
                    rt,
                    ut,
                    pt,
                    wt,
                    bt,
                    s;
                    for (l = gt; l <= ni; l++) {
                        l < p ? (s = l < v ? 0 : v === p ? 1 : (v - l) / (v - p), w = k - (k - ot) * s, tt = d - (d - st) * s, it = g - (g - ht) * s, rt = nt - (nt - ct) * s) : (s = l > y ? 1 : p === y ? 0 : (p - l) / (p - y), w = ot - (ot - lt) * s, tt = st - (st - at) * s, it = ht - (ht - vt) * s, rt = ct - (ct - yt) * s);
                        s = l < v ? 0 : l > y ? 1 : (v - l) / (v - y);
                        ut = k - (k - lt) * s;
                        pt = d - (d - at) * s;
                        wt = g - (g - vt) * s;
                        bt = nt - (nt - yt) * s;
                        var kt = Math.round(Math.min(w, ut)),
                        ti = Math.round(Math.max(w, ut)),
                        ft = dt * l + kt * 4;
                        for (et = kt; et <= ti; et++) s = (w - et) / (w - ut),
                        s = s < 0 ? 0 : s > 1 ? 1 : s,
                        b[ft++] = tt - (tt - pt) * s | 0,
                        b[ft++] = it - (it - wt) * s | 0,
                        b[ft++] = rt - (rt - bt) * s | 0,
                        b[ft++] = 255
                    }
                }
            }
            function t(t, i, r) {
                var e = i.coords,
                o = i.colors,
                f, c, u, h;
                switch (i.type) {
                case "lattice":
                    var s = i.verticesPerRow,
                    l = Math.floor(e.length / s) - 1,
                    a = s - 1;
                    for (f = 0; f < l; f++) for (u = f * s, h = 0; h < a; h++, u++) n(t, r, e[u], e[u + 1], e[u + s], o[u], o[u + 1], o[u + s]),
                    n(t, r, e[u + s + 1], e[u + 1], e[u + s], o[u + s + 1], o[u + 1], o[u + s]);
                    break;
                case "triangles":
                    for (f = 0, c = e.length; f < c; f += 3) n(t, r, e[f], e[f + 1], e[f + 2], o[f], o[f + 1], o[f + 2]);
                    break;
                default:
                    throw new Error("illegal figure");
                }
            }
            function i(n, i, r, u, e, o, s) {
                var tt = 1.1,
                it = 3e3,
                c = 2,
                b = Math.floor(n[0]),
                k = Math.floor(n[1]),
                rt = Math.ceil(n[2]) - b,
                ut = Math.ceil(n[3]) - k,
                y = Math.min(Math.ceil(Math.abs(rt * i[0] * tt)), it),
                p = Math.min(Math.ceil(Math.abs(ut * i[1] * tt)), it),
                d = rt / y,
                g = ut / p,
                ft = {
                    coords: r,
                    colors: u,
                    offsetX: -b,
                    offsetY: -k,
                    scaleX: 1 / d,
                    scaleY: 1 / g
                },
                et = y + c * 2,
                ot = p + c * 2,
                v,
                l,
                h,
                st,
                nt,
                w,
                a;
                if (f.WebGLUtils.isEnabled) v = f.WebGLUtils.drawFigures(y, p, o, e, ft),
                l = s.getCanvas("mesh", et, ot, !1),
                l.context.drawImage(v, c, c),
                v = l.canvas;
                else {
                    if (l = s.getCanvas("mesh", et, ot, !1), nt = l.context, w = nt.createImageData(y, p), o) for (a = w.data, h = 0, st = a.length; h < st; h += 4) a[h] = o[0],
                    a[h + 1] = o[1],
                    a[h + 2] = o[2],
                    a[h + 3] = 255;
                    for (h = 0; h < e.length; h++) t(w, e[h], ft);
                    nt.putImageData(w, c, c);
                    v = l.canvas
                }
                return {
                    canvas: v,
                    offsetX: b - c * d,
                    offsetY: k - c * g,
                    scaleX: d,
                    scaleY: g
                }
            }
            return i
        } ();
        u.Mesh = {
            fromIR: function(n) {
                var i = n[2],
                u = n[3],
                f = n[4],
                o = n[5],
                t = n[6],
                s = n[8];
                return {
                    type: "Pattern",
                    getPattern: function(n, h, c) {
                        var l, v, a;
                        return c ? l = r.Util.singularValueDecompose2dScale(n.mozCurrentTransform) : (l = r.Util.singularValueDecompose2dScale(h.baseTransform), t && (v = r.Util.singularValueDecompose2dScale(t), l = [l[0] * v[0], l[1] * v[1]])),
                        a = e(o, l, i, u, f, c ? null: s, h.cachedCanvases),
                        c || (n.setTransform.apply(n, h.baseTransform), t && n.transform.apply(n, t)),
                        n.translate(a.offsetX, a.offsetY),
                        n.scale(a.scaleX, a.scaleY),
                        n.createPattern(a.canvas, "no-repeat")
                    }
                }
            }
        };
        u.Dummy = {
            fromIR: function() {
                return {
                    type: "Pattern",
                    getPattern: function() {
                        return "hotpink"
                    }
                }
            }
        };
        o = function() {
            function i(n, t, i, r, u) {
                this.operatorList = n[2];
                this.matrix = n[3] || [1, 0, 0, 1, 0, 0];
                this.bbox = n[4];
                this.xstep = n[5];
                this.ystep = n[6];
                this.paintType = n[7];
                this.tilingType = n[8];
                this.color = t;
                this.canvasGraphicsFactory = r;
                this.baseTransform = u;
                this.type = "Pattern";
                this.ctx = i
            }
            var n = {
                COLORED: 1,
                UNCOLORED: 2
            },
            t = 3e3;
            return i.prototype = {
                createPatternCanvas: function(n) {
                    var k = this.operatorList,
                    u = this.bbox,
                    c = this.xstep,
                    l = this.ystep,
                    d = this.paintType,
                    g = this.tilingType,
                    nt = this.color,
                    tt = this.canvasGraphicsFactory,
                    b;
                    r.info("TilingType: " + g);
                    var s = u[0],
                    h = u[1],
                    it = u[2],
                    rt = u[3],
                    f = [s, h],
                    a = [s + c, h + l],
                    e = a[0] - f[0],
                    o = a[1] - f[1],
                    v = r.Util.singularValueDecompose2dScale(this.matrix),
                    y = r.Util.singularValueDecompose2dScale(this.baseTransform),
                    p = [v[0] * y[0], v[1] * y[1]];
                    e = Math.min(Math.ceil(Math.abs(e * p[0])), t);
                    o = Math.min(Math.ceil(Math.abs(o * p[1])), t);
                    var w = n.cachedCanvases.getCanvas("pattern", e, o, !0),
                    ut = w.context,
                    i = tt.createCanvasGraphics(ut);
                    return i.groupLevel = n.groupLevel,
                    this.setFillAndStrokeStyleToContext(i, d, nt),
                    this.setScale(e, o, c, l),
                    this.transformToScale(i),
                    b = [1, 0, 0, 1, -f[0], -f[1]],
                    i.transform.apply(i, b),
                    this.clipBbox(i, u, s, h, it, rt),
                    i.executeOperatorList(k),
                    w.canvas
                },
                setScale: function(n, t, i, r) {
                    this.scale = [n / i, t / r]
                },
                transformToScale: function(n) {
                    var t = this.scale,
                    i = [t[0], 0, 0, t[1], 0, 0];
                    n.transform.apply(n, i)
                },
                scaleToContext: function() {
                    var n = this.scale;
                    this.ctx.scale(1 / n[0], 1 / n[1])
                },
                clipBbox: function(n, t, i, r, u, f) {
                    if (Array.isArray(t) && t.length === 4) {
                        var e = u - i,
                        o = f - r;
                        n.ctx.rect(i, r, e, o);
                        n.clip();
                        n.endPath()
                    }
                },
                setFillAndStrokeStyleToContext: function(t, i, u) {
                    var o = t.ctx,
                    s = t.current,
                    f, e;
                    switch (i) {
                    case n.COLORED:
                        f = this.ctx;
                        o.fillStyle = f.fillStyle;
                        o.strokeStyle = f.strokeStyle;
                        s.fillColor = f.fillStyle;
                        s.strokeColor = f.strokeStyle;
                        break;
                    case n.UNCOLORED:
                        e = r.Util.makeCssRgb(u[0], u[1], u[2]);
                        o.fillStyle = e;
                        o.strokeStyle = e;
                        s.fillColor = e;
                        s.strokeColor = e;
                        break;
                    default:
                        throw new r.FormatError("Unsupported paint type: " + i);
                    }
                },
                getPattern: function(n, t) {
                    var i = this.createPatternCanvas(t);
                    return n = this.ctx,
                    n.setTransform.apply(n, this.baseTransform),
                    n.transform.apply(n, this.matrix),
                    this.scaleToContext(),
                    n.createPattern(i, "repeat")
                }
            },
            i
        } ();
        t.getShadingPatternFromIR = s;
        t.TilingPattern = o
    },
    function(n, t, i) {
        "use strict";
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.PDFDataTransportStream = undefined;
        var r = i(0),
        u = function() {
            function n(n, t) {
                var u = this,
                i, f;
                r.assert(t);
                this._queuedChunks = [];
                i = n.initialData;
                i && i.length > 0 && (f = new Uint8Array(i).buffer, this._queuedChunks.push(f));
                this._pdfDataRangeTransport = t;
                this._isRangeSupported = !n.disableRange;
                this._isStreamingSupported = !n.disableStream;
                this._contentLength = n.length;
                this._fullRequestReader = null;
                this._rangeReaders = [];
                this._pdfDataRangeTransport.addRangeListener(function(n, t) {
                    u._onReceiveData({
                        begin: n,
                        chunk: t
                    })
                });
                this._pdfDataRangeTransport.addProgressListener(function(n) {
                    u._onProgress({
                        loaded: n
                    })
                });
                this._pdfDataRangeTransport.addProgressiveReadListener(function(n) {
                    u._onReceiveData({
                        chunk: n
                    })
                });
                this._pdfDataRangeTransport.transportReady()
            }
            function t(n, t) {
                this._stream = n;
                this._done = !1;
                this._queuedChunks = t || [];
                this._requests = [];
                this._headersReady = Promise.resolve();
                n._fullRequestReader = this;
                this.onProgress = null
            }
            function i(n, t, i) {
                this._stream = n;
                this._begin = t;
                this._end = i;
                this._queuedChunk = null;
                this._requests = [];
                this._done = !1;
                this.onProgress = null
            }
            return n.prototype = {
                _onReceiveData: function(n) {
                    var t = new Uint8Array(n.chunk).buffer,
                    i;
                    n.begin === undefined ? this._fullRequestReader ? this._fullRequestReader._enqueue(t) : this._queuedChunks.push(t) : (i = this._rangeReaders.some(function(i) {
                        return i._begin !== n.begin ? !1 : (i._enqueue(t), !0)
                    }), r.assert(i))
                },
                _onProgress: function(n) {
                    if (this._rangeReaders.length > 0) {
                        var t = this._rangeReaders[0];
                        if (t.onProgress) t.onProgress({
                            loaded: n.loaded
                        })
                    }
                },
                _removeRangeReader: function(n) {
                    var t = this._rangeReaders.indexOf(n);
                    t >= 0 && this._rangeReaders.splice(t, 1)
                },
                getFullReader: function() {
                    r.assert(!this._fullRequestReader);
                    var n = this._queuedChunks;
                    return this._queuedChunks = null,
                    new t(this, n)
                },
                getRangeReader: function(n, t) {
                    var r = new i(this, n, t);
                    return this._pdfDataRangeTransport.requestDataRange(n, t),
                    this._rangeReaders.push(r),
                    r
                },
                cancelAllRequests: function(n) {
                    this._fullRequestReader && this._fullRequestReader.cancel(n);
                    var t = this._rangeReaders.slice(0);
                    t.forEach(function(t) {
                        t.cancel(n)
                    });
                    this._pdfDataRangeTransport.abort()
                }
            },
            t.prototype = {
                _enqueue: function(n) {
                    if (!this._done) {
                        if (this._requests.length > 0) {
                            var t = this._requests.shift();
                            t.resolve({
                                value: n,
                                done: !1
                            });
                            return
                        }
                        this._queuedChunks.push(n)
                    }
                },
                get headersReady() {
                    return this._headersReady
                },
                get isRangeSupported() {
                    return this._stream._isRangeSupported
                },
                get isStreamingSupported() {
                    return this._stream._isStreamingSupported
                },
                get contentLength() {
                    return this._stream._contentLength
                },
                read: function() {
                    var t, n;
                    return this._queuedChunks.length > 0 ? (t = this._queuedChunks.shift(), Promise.resolve({
                        value: t,
                        done: !1
                    })) : this._done ? Promise.resolve({
                        value: undefined,
                        done: !0
                    }) : (n = r.createPromiseCapability(), this._requests.push(n), n.promise)
                },
                cancel: function() {
                    this._done = !0;
                    this._requests.forEach(function(n) {
                        n.resolve({
                            value: undefined,
                            done: !0
                        })
                    });
                    this._requests = []
                }
            },
            i.prototype = {
                _enqueue: function(n) {
                    if (!this._done) {
                        if (this._requests.length === 0) this._queuedChunk = n;
                        else {
                            var t = this._requests.shift();
                            t.resolve({
                                value: n,
                                done: !1
                            });
                            this._requests.forEach(function(n) {
                                n.resolve({
                                    value: undefined,
                                    done: !0
                                })
                            });
                            this._requests = []
                        }
                        this._done = !0;
                        this._stream._removeRangeReader(this)
                    }
                },
                get isStreamingSupported() {
                    return ! 1
                },
                read: function() {
                    var t, n;
                    return this._queuedChunk ? (t = this._queuedChunk, this._queuedChunk = null, Promise.resolve({
                        value: t,
                        done: !1
                    })) : this._done ? Promise.resolve({
                        value: undefined,
                        done: !0
                    }) : (n = r.createPromiseCapability(), this._requests.push(n), n.promise)
                },
                cancel: function() {
                    this._done = !0;
                    this._requests.forEach(function(n) {
                        n.resolve({
                            value: undefined,
                            done: !0
                        })
                    });
                    this._requests = [];
                    this._stream._removeRangeReader(this)
                }
            },
            n
        } ();
        t.PDFDataTransportStream = u
    },
    function(n, t, i) {
        "use strict";
        function f(n, t) {
            if (!n) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
            return t && (typeof t == "object" || typeof t == "function") ? t: n
        }
        function e(n, t) {
            if (typeof t != "function" && t !== null) throw new TypeError("Super expression must either be null or a function, not " + typeof t);
            n.prototype = Object.create(t && t.prototype, {
                constructor: {
                    value: n,
                    enumerable: !1,
                    writable: !0,
                    configurable: !0
                }
            });
            t && (Object.setPrototypeOf ? Object.setPrototypeOf(n, t) : n.__proto__ = t)
        }
        function u(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        function o(n, t) {
            return {
                protocol: n.protocol,
                auth: n.auth,
                host: n.hostname,
                port: n.port,
                path: n.path,
                method: "GET",
                headers: t
            }
        }
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.PDFNodeStream = undefined;
        var s = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        r = i(0),
        y = i(38);
        var h = require("fs"),
        c = require("http"),
        l = require("https"),
        p = require("url"),
        w = function() {
            function n(t) {
                u(this, n);
                this.options = t;
                this.source = t.source;
                this.url = p.parse(this.source.url);
                this.isHttp = this.url.protocol === "http:" || this.url.protocol === "https:";
                this.isFsUrl = this.url.protocol === "file:" || !this.url.host;
                this.httpHeaders = this.isHttp && this.source.httpHeaders || {};
                this._fullRequest = null;
                this._rangeRequestReaders = []
            }
            return s(n, [{
                key: "getFullReader",
                value: function() {
                    return r.assert(!this._fullRequest),
                    this._fullRequest = this.isFsUrl ? new d(this) : new b(this),
                    this._fullRequest
                }
            },
            {
                key: "getRangeReader",
                value: function(n, t) {
                    var i = this.isFsUrl ? new g(this, n, t) : new k(this, n, t);
                    return this._rangeRequestReaders.push(i),
                    i
                }
            },
            {
                key: "cancelAllRequests",
                value: function(n) {
                    this._fullRequest && this._fullRequest.cancel(n);
                    var t = this._rangeRequestReaders.slice(0);
                    t.forEach(function(t) {
                        t.cancel(n)
                    })
                }
            }]),
            n
        } (),
        a = function() {
            function n(t) {
                u(this, n);
                this._url = t.url;
                this._done = !1;
                this._errored = !1;
                this._reason = null;
                this.onProgress = null;
                this._contentLength = t.source.length;
                this._loaded = 0;
                this._disableRange = t.options.disableRange || !1;
                this._rangeChunkSize = t.source.rangeChunkSize;
                this._rangeChunkSize || this._disableRange || (this._disableRange = !0);
                this._isStreamingSupported = !t.source.disableStream;
                this._isRangeSupported = !t.options.disableRange;
                this._readableStream = null;
                this._readCapability = r.createPromiseCapability();
                this._headersCapability = r.createPromiseCapability()
            }
            return s(n, [{
                key: "read",
                value: function() {
                    var n = this;
                    return this._readCapability.promise.then(function() {
                        var t, i;
                        if (n._done) return Promise.resolve({
                            value: undefined,
                            done: !0
                        });
                        if (n._errored) return Promise.reject(n._reason);
                        if (t = n._readableStream.read(), t === null) return n._readCapability = r.createPromiseCapability(),
                        n.read();
                        if (n._loaded += t.length, n.onProgress) n.onProgress({
                            loaded: n._loaded,
                            total: n._contentLength
                        });
                        return i = new Uint8Array(t).buffer,
                        Promise.resolve({
                            value: i,
                            done: !1
                        })
                    })
                }
            },
            {
                key: "cancel",
                value: function(n) {
                    if (!this._readableStream) {
                        this._error(n);
                        return
                    }
                    this._readableStream.destroy(n)
                }
            },
            {
                key: "_error",
                value: function(n) {
                    this._errored = !0;
                    this._reason = n;
                    this._readCapability.resolve()
                }
            },
            {
                key: "_setReadableStream",
                value: function(n) {
                    var t = this;
                    this._readableStream = n;
                    n.on("readable",
                    function() {
                        t._readCapability.resolve()
                    });
                    n.on("end",
                    function() {
                        n.destroy();
                        t._done = !0;
                        t._readCapability.resolve()
                    });
                    n.on("error",
                    function(n) {
                        t._error(n)
                    }); ! this._isStreamingSupported && this._isRangeSupported && this._error(new r.AbortException("streaming is disabled"));
                    this._errored && this._readableStream.destroy(this._reason)
                }
            },
            {
                key: "headersReady",
                get: function() {
                    return this._headersCapability.promise
                }
            },
            {
                key: "contentLength",
                get: function() {
                    return this._contentLength
                }
            },
            {
                key: "isRangeSupported",
                get: function() {
                    return this._isRangeSupported
                }
            },
            {
                key: "isStreamingSupported",
                get: function() {
                    return this._isStreamingSupported
                }
            }]),
            n
        } (),
        v = function() {
            function n(t) {
                u(this, n);
                this._url = t.url;
                this._done = !1;
                this._errored = !1;
                this._reason = null;
                this.onProgress = null;
                this._loaded = 0;
                this._readableStream = null;
                this._readCapability = r.createPromiseCapability();
                this._isStreamingSupported = !t.source.disableStream
            }
            return s(n, [{
                key: "read",
                value: function() {
                    var n = this;
                    return this._readCapability.promise.then(function() {
                        var t, i;
                        if (n._done) return Promise.resolve({
                            value: undefined,
                            done: !0
                        });
                        if (n._errored) return Promise.reject(n._reason);
                        if (t = n._readableStream.read(), t === null) return n._readCapability = r.createPromiseCapability(),
                        n.read();
                        if (n._loaded += t.length, n.onProgress) n.onProgress({
                            loaded: n._loaded
                        });
                        return i = new Uint8Array(t).buffer,
                        Promise.resolve({
                            value: i,
                            done: !1
                        })
                    })
                }
            },
            {
                key: "cancel",
                value: function(n) {
                    if (!this._readableStream) {
                        this._error(n);
                        return
                    }
                    this._readableStream.destroy(n)
                }
            },
            {
                key: "_error",
                value: function(n) {
                    this._errored = !0;
                    this._reason = n;
                    this._readCapability.resolve()
                }
            },
            {
                key: "_setReadableStream",
                value: function(n) {
                    var t = this;
                    this._readableStream = n;
                    n.on("readable",
                    function() {
                        t._readCapability.resolve()
                    });
                    n.on("end",
                    function() {
                        n.destroy();
                        t._done = !0;
                        t._readCapability.resolve()
                    });
                    n.on("error",
                    function(n) {
                        t._error(n)
                    });
                    this._errored && this._readableStream.destroy(this._reason)
                }
            },
            {
                key: "isStreamingSupported",
                get: function() {
                    return this._isStreamingSupported
                }
            }]),
            n
        } ();
        var b = function(n) {
            function t(n) {
                u(this, t);
                var i = f(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n)),
                r = function(t) {
                    i._headersCapability.resolve();
                    i._setReadableStream(t);
                    var r = y.validateRangeRequestCapabilities({
                        getResponseHeader: function(n) {
                            return i._readableStream.headers[n.toLowerCase()]
                        },
                        isHttp: n.isHttp,
                        rangeChunkSize: i._rangeChunkSize,
                        disableRange: i._disableRange
                    }),
                    u = r.allowRangeRequests,
                    f = r.suggestedLength;
                    u && (i._isRangeSupported = !0);
                    i._contentLength = f
                };
                i._request = null;
                i._request = i._url.protocol === "http:" ? c.request(o(i._url, n.httpHeaders), r) : l.request(o(i._url, n.httpHeaders), r);
                i._request.on("error",
                function(n) {
                    i._errored = !0;
                    i._reason = n;
                    i._headersCapability.reject(n)
                });
                return i._request.end(),
                i
            }
            return e(t, n),
            t
        } (a),
        k = function(n) {
            function t(n, i, r) {
                var e, s, h;
                u(this, t);
                e = f(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n));
                e._httpHeaders = {};
                for (s in n.httpHeaders)(h = n.httpHeaders[s], typeof h != "undefined") && (e._httpHeaders[s] = h);
                e._httpHeaders.Range = "bytes=" + i + "-" + (r - 1);
                e._request = null;
                e._request = e._url.protocol === "http:" ? c.request(o(e._url, e._httpHeaders),
                function(n) {
                    e._setReadableStream(n)
                }) : l.request(o(e._url, e._httpHeaders),
                function(n) {
                    e._setReadableStream(n)
                });
                e._request.on("error",
                function(n) {
                    e._errored = !0;
                    e._reason = n
                });
                return e._request.end(),
                e
            }
            return e(t, n),
            t
        } (v),
        d = function(n) {
            function t(n) {
                u(this, t);
                var i = f(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n)),
                r = decodeURI(i._url.path);
                return h.lstat(r,
                function(n, t) {
                    if (n) {
                        i._errored = !0;
                        i._reason = n;
                        i._headersCapability.reject(n);
                        return
                    }
                    i._contentLength = t.size;
                    i._setReadableStream(h.createReadStream(r));
                    i._headersCapability.resolve()
                }),
                i
            }
            return e(t, n),
            t
        } (a),
        g = function(n) {
            function t(n, i, r) {
                u(this, t);
                var e = f(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, n));
                return e._setReadableStream(h.createReadStream(decodeURI(e._url.path), {
                    start: i,
                    end: r - 1
                })),
                e
            }
            return e(t, n),
            t
        } (v);
        t.PDFNodeStream = w
    },
    function(n, t, i) {
        "use strict";
        function e(n, t) {
            if (! (n instanceof t)) throw new TypeError("Cannot call a class as a function");
        }
        function o(n, t) {
            return {
                method: "GET",
                headers: n,
                mode: "cors",
                credentials: t ? "include": "same-origin",
                redirect: "follow"
            }
        }
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.PDFFetchStream = undefined;
        var f = function() {
            function n(n, t) {
                for (var i, r = 0; r < t.length; r++) i = t[r],
                i.enumerable = i.enumerable || !1,
                i.configurable = !0,
                "value" in i && (i.writable = !0),
                Object.defineProperty(n, i.key, i)
            }
            return function(t, i, r) {
                return i && n(t.prototype, i),
                r && n(t, r),
                t
            }
        } (),
        u = i(0),
        r = i(38);
        var s = function() {
            function n(t) {
                e(this, n);
                this.options = t;
                this.source = t.source;
                this.isHttp = /^https?:/i.test(this.source.url);
                this.httpHeaders = this.isHttp && this.source.httpHeaders || {};
                this._fullRequestReader = null;
                this._rangeRequestReaders = []
            }
            return f(n, [{
                key: "getFullReader",
                value: function() {
                    return u.assert(!this._fullRequestReader),
                    this._fullRequestReader = new h(this),
                    this._fullRequestReader
                }
            },
            {
                key: "getRangeReader",
                value: function(n, t) {
                    var i = new c(this, n, t);
                    return this._rangeRequestReaders.push(i),
                    i
                }
            },
            {
                key: "cancelAllRequests",
                value: function(n) {
                    this._fullRequestReader && this._fullRequestReader.cancel(n);
                    var t = this._rangeRequestReaders.slice(0);
                    t.forEach(function(t) {
                        t.cancel(n)
                    })
                }
            }]),
            n
        } (),
        h = function() {
            function n(t) {
                var i = this,
                f, s, h;
                e(this, n);
                this._stream = t;
                this._reader = null;
                this._loaded = 0;
                this._withCredentials = t.source.withCredentials;
                this._contentLength = this._stream.source.length;
                this._headersCapability = u.createPromiseCapability();
                this._disableRange = this._stream.options.disableRange;
                this._rangeChunkSize = this._stream.source.rangeChunkSize;
                this._rangeChunkSize || this._disableRange || (this._disableRange = !0);
                this._isRangeSupported = !this._stream.options.disableRange;
                this._isStreamingSupported = !this._stream.source.disableStream;
                this._headers = new Headers;
                for (f in this._stream.httpHeaders)(s = this._stream.httpHeaders[f], typeof s != "undefined") && this._headers.append(f, s);
                h = this._stream.source.url;
                fetch(h, o(this._headers, this._withCredentials)).then(function(n) {
                    if (!r.validateResponseStatus(n.status)) throw r.createResponseStatusError(n.status, h);
                    i._reader = n.body.getReader();
                    i._headersCapability.resolve();
                    var t = r.validateRangeRequestCapabilities({
                        getResponseHeader: function(t) {
                            return n.headers.get(t)
                        },
                        isHttp: i._stream.isHttp,
                        rangeChunkSize: i._rangeChunkSize,
                        disableRange: i._disableRange
                    }),
                    f = t.allowRangeRequests,
                    e = t.suggestedLength;
                    i._contentLength = e;
                    i._isRangeSupported = f; ! i._isStreamingSupported && i._isRangeSupported && i.cancel(new u.AbortException("streaming is disabled"))
                }).
                catch(this._headersCapability.reject);
                this.onProgress = null
            }
            return f(n, [{
                key: "read",
                value: function() {
                    var n = this;
                    return this._headersCapability.promise.then(function() {
                        return n._reader.read().then(function(t) {
                            var i = t.value,
                            r = t.done,
                            u;
                            if (r) return Promise.resolve({
                                value: i,
                                done: r
                            });
                            if (n._loaded += i.byteLength, n.onProgress) n.onProgress({
                                loaded: n._loaded,
                                total: n._contentLength
                            });
                            return u = new Uint8Array(i).buffer,
                            Promise.resolve({
                                value: u,
                                done: !1
                            })
                        })
                    })
                }
            },
            {
                key: "cancel",
                value: function(n) {
                    this._reader && this._reader.cancel(n)
                }
            },
            {
                key: "headersReady",
                get: function() {
                    return this._headersCapability.promise
                }
            },
            {
                key: "contentLength",
                get: function() {
                    return this._contentLength
                }
            },
            {
                key: "isRangeSupported",
                get: function() {
                    return this._isRangeSupported
                }
            },
            {
                key: "isStreamingSupported",
                get: function() {
                    return this._isStreamingSupported
                }
            }]),
            n
        } (),
        c = function() {
            function n(t, i, f) {
                var l = this,
                s, h, a, c;
                e(this, n);
                this._stream = t;
                this._reader = null;
                this._loaded = 0;
                this._withCredentials = t.source.withCredentials;
                this._readCapability = u.createPromiseCapability();
                this._isStreamingSupported = !t.source.disableStream;
                this._headers = new Headers;
                for (s in this._stream.httpHeaders)(h = this._stream.httpHeaders[s], typeof h != "undefined") && this._headers.append(s, h);
                a = i + "-" + (f - 1);
                this._headers.append("Range", "bytes=" + a);
                c = this._stream.source.url;
                fetch(c, o(this._headers, this._withCredentials)).then(function(n) {
                    if (!r.validateResponseStatus(n.status)) throw r.createResponseStatusError(n.status, c);
                    l._readCapability.resolve();
                    l._reader = n.body.getReader()
                });
                this.onProgress = null
            }
            return f(n, [{
                key: "read",
                value: function() {
                    var n = this;
                    return this._readCapability.promise.then(function() {
                        return n._reader.read().then(function(t) {
                            var i = t.value,
                            r = t.done,
                            u;
                            if (r) return Promise.resolve({
                                value: i,
                                done: r
                            });
                            if (n._loaded += i.byteLength, n.onProgress) n.onProgress({
                                loaded: n._loaded
                            });
                            return u = new Uint8Array(i).buffer,
                            Promise.resolve({
                                value: u,
                                done: !1
                            })
                        })
                    })
                }
            },
            {
                key: "cancel",
                value: function(n) {
                    this._reader && this._reader.cancel(n)
                }
            },
            {
                key: "isStreamingSupported",
                get: function() {
                    return this._isStreamingSupported
                }
            }]),
            n
        } ();
        t.PDFFetchStream = s
    },
    function(n, t, i) {
        "use strict";
        function p(n) {
            return n && n.__esModule ? n: {
                "default": n
            }
        }
        function e(n, t) {
            this.url = n;
            t = t || {};
            this.isHttp = /^https?:/i.test(n);
            this.httpHeaders = this.isHttp && t.httpHeaders || {};
            this.withCredentials = t.withCredentials || !1;
            this.getXhr = t.getXhr ||
            function() {
                return new XMLHttpRequest
            };
            this.currXhrId = 0;
            this.pendingRequests = Object.create(null);
            this.loadedRequests = Object.create(null)
        }
        function s(n) {
            var t = n.response,
            i;
            return typeof t != "string" ? t: (i = r.stringToBytes(t), i.buffer)
        }
        function c(n) {
            this._options = n;
            var t = n.source;
            this._manager = new e(t.url, {
                httpHeaders: t.httpHeaders,
                withCredentials: t.withCredentials
            });
            this._rangeChunkSize = t.rangeChunkSize;
            this._fullRequestReader = null;
            this._rangeRequestReaders = []
        }
        function l(n, t) {
            this._manager = n;
            var i = t.source,
            u = {
                onHeadersReceived: this._onHeadersReceived.bind(this),
                onProgressiveData: i.disableStream ? null: this._onProgressiveData.bind(this),
                onDone: this._onDone.bind(this),
                onError: this._onError.bind(this),
                onProgress: this._onProgress.bind(this)
            };
            this._url = i.url;
            this._fullRequestId = n.requestFull(u);
            this._headersReceivedCapability = r.createPromiseCapability();
            this._disableRange = t.disableRange || !1;
            this._contentLength = i.length;
            this._rangeChunkSize = i.rangeChunkSize;
            this._rangeChunkSize || this._disableRange || (this._disableRange = !0);
            this._isStreamingSupported = !1;
            this._isRangeSupported = !1;
            this._cachedChunks = [];
            this._requests = [];
            this._done = !1;
            this._storedError = undefined;
            this.onProgress = null
        }
        function a(n, t, i) {
            this._manager = n;
            var r = {
                onDone: this._onDone.bind(this),
                onProgress: this._onProgress.bind(this)
            };
            this._requestId = n.requestRange(t, i, r);
            this._requests = [];
            this._queuedChunk = null;
            this._done = !1;
            this.onProgress = null;
            this.onClosed = null
        }
        var u, f, h;
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        t.NetworkManager = t.PDFNetworkStream = undefined;
        var r = i(0),
        o = i(38),
        v = i(14),
        y = p(v);
        u = 200;
        f = 206;
        h = function() {
            try {
                var n = new XMLHttpRequest;
                return n.open("GET", y.
            default.location.href),
                n.responseType = "moz-chunked-arraybuffer",
                n.responseType === "moz-chunked-arraybuffer"
            } catch(t) {
                return ! 1
            }
        } ();
        e.prototype = {
            requestRange: function(n, t, i) {
                var r = {
                    begin: n,
                    end: t
                };
                for (var u in i) r[u] = i[u];
                return this.request(r)
            },
            requestFull: function(n) {
                return this.request(n)
            },
            request: function(n) {
                var t = this.getXhr(),
                r = this.currXhrId++,
                i = this.pendingRequests[r] = {
                    xhr: t
                },
                u,
                f,
                e,
                o;
                t.open("GET", this.url);
                t.withCredentials = this.withCredentials;
                for (u in this.httpHeaders)(f = this.httpHeaders[u], typeof f != "undefined") && t.setRequestHeader(u, f);
                return this.isHttp && "begin" in n && "end" in n ? (e = n.begin + "-" + (n.end - 1), t.setRequestHeader("Range", "bytes=" + e), i.expectedStatus = 206) : i.expectedStatus = 200,
                o = h && !!n.onProgressiveData,
                o ? (t.responseType = "moz-chunked-arraybuffer", i.onProgressiveData = n.onProgressiveData, i.mozChunked = !0) : t.responseType = "arraybuffer",
                n.onError && (t.onerror = function() {
                    n.onError(t.status)
                }),
                t.onreadystatechange = this.onStateChange.bind(this, r),
                t.onprogress = this.onProgress.bind(this, r),
                i.onHeadersReceived = n.onHeadersReceived,
                i.onDone = n.onDone,
                i.onError = n.onError,
                i.onProgress = n.onProgress,
                t.send(null),
                r
            },
            onProgress: function(n, t) {
                var i = this.pendingRequests[n],
                u,
                r;
                if (i) {
                    if (i.mozChunked) {
                        u = s(i.xhr);
                        i.onProgressiveData(u)
                    }
                    r = i.onProgress;
                    r && r(t)
                }
            },
            onStateChange: function(n) {
                var t = this.pendingRequests[n],
                i,
                r,
                o,
                e;
                if (t && (i = t.xhr, i.readyState >= 2 && t.onHeadersReceived && (t.onHeadersReceived(), delete t.onHeadersReceived), i.readyState === 4) && n in this.pendingRequests) {
                    if (delete this.pendingRequests[n], i.status === 0 && this.isHttp) {
                        if (t.onError) t.onError(i.status);
                        return
                    }
                    if (r = i.status || u, o = r === u && t.expectedStatus === f, !o && r !== t.expectedStatus) {
                        if (t.onError) t.onError(i.status);
                        return
                    }
                    if (this.loadedRequests[n] = !0, e = s(i), r === f) {
                        var h = i.getResponseHeader("Content-Range"),
                        c = /bytes (\d+)-(\d+)\/(\d+)/.exec(h),
                        l = parseInt(c[1], 10);
                        t.onDone({
                            begin: l,
                            chunk: e
                        })
                    } else if (t.onProgressiveData) t.onDone(null);
                    else if (e) t.onDone({
                        begin: 0,
                        chunk: e
                    });
                    else if (t.onError) t.onError(i.status)
                }
            },
            hasPendingRequests: function() {
                for (var n in this.pendingRequests) return ! 0;
                return ! 1
            },
            getRequestXhr: function(n) {
                return this.pendingRequests[n].xhr
            },
            isStreamingRequest: function(n) {
                return !! this.pendingRequests[n].onProgressiveData
            },
            isPendingRequest: function(n) {
                return n in this.pendingRequests
            },
            isLoadedRequest: function(n) {
                return n in this.loadedRequests
            },
            abortAllRequests: function() {
                for (var n in this.pendingRequests) this.abortRequest(n | 0)
            },
            abortRequest: function(n) {
                var t = this.pendingRequests[n].xhr;
                delete this.pendingRequests[n];
                t.abort()
            }
        };
        c.prototype = {
            _onRangeRequestReaderClosed: function(n) {
                var t = this._rangeRequestReaders.indexOf(n);
                t >= 0 && this._rangeRequestReaders.splice(t, 1)
            },
            getFullReader: function() {
                return r.assert(!this._fullRequestReader),
                this._fullRequestReader = new l(this._manager, this._options),
                this._fullRequestReader
            },
            getRangeReader: function(n, t) {
                var i = new a(this._manager, n, t);
                return i.onClosed = this._onRangeRequestReaderClosed.bind(this),
                this._rangeRequestReaders.push(i),
                i
            },
            cancelAllRequests: function(n) {
                this._fullRequestReader && this._fullRequestReader.cancel(n);
                var t = this._rangeRequestReaders.slice(0);
                t.forEach(function(t) {
                    t.cancel(n)
                })
            }
        };
        l.prototype = {
            _onHeadersReceived: function() {
                var n = this._fullRequestId,
                r = this._manager.getRequestXhr(n),
                i = o.validateRangeRequestCapabilities({
                    getResponseHeader: function(n) {
                        return r.getResponseHeader(n)
                    },
                    isHttp: this._manager.isHttp,
                    rangeChunkSize: this._rangeChunkSize,
                    disableRange: this._disableRange
                }),
                u = i.allowRangeRequests,
                f = i.suggestedLength,
                t;
                this._contentLength = f || this._contentLength;
                u && (this._isRangeSupported = !0);
                t = this._manager;
                t.isStreamingRequest(n) ? this._isStreamingSupported = !0 : this._isRangeSupported && t.abortRequest(n);
                this._headersReceivedCapability.resolve()
            },
            _onProgressiveData: function(n) {
                if (this._requests.length > 0) {
                    var t = this._requests.shift();
                    t.resolve({
                        value: n,
                        done: !1
                    })
                } else this._cachedChunks.push(n)
            },
            _onDone: function(n) { (n && this._onProgressiveData(n.chunk), this._done = !0, this._cachedChunks.length > 0) || (this._requests.forEach(function(n) {
                    n.resolve({
                        value: undefined,
                        done: !0
                    })
                }), this._requests = [])
            },
            _onError: function(n) {
                var i = this._url,
                t = o.createResponseStatusError(n, i);
                this._storedError = t;
                this._headersReceivedCapability.reject(t);
                this._requests.forEach(function(n) {
                    n.reject(t)
                });
                this._requests = [];
                this._cachedChunks = []
            },
            _onProgress: function(n) {
                if (this.onProgress) this.onProgress({
                    loaded: n.loaded,
                    total: n.lengthComputable ? n.total: this._contentLength
                })
            },
            get isRangeSupported() {
                return this._isRangeSupported
            },
            get isStreamingSupported() {
                return this._isStreamingSupported
            },
            get contentLength() {
                return this._contentLength
            },
            get headersReady() {
                return this._headersReceivedCapability.promise
            },
            read: function() {
                var t, n;
                return this._storedError ? Promise.reject(this._storedError) : this._cachedChunks.length > 0 ? (t = this._cachedChunks.shift(), Promise.resolve({
                    value: t,
                    done: !1
                })) : this._done ? Promise.resolve({
                    value: undefined,
                    done: !0
                }) : (n = r.createPromiseCapability(), this._requests.push(n), n.promise)
            },
            cancel: function(n) {
                this._done = !0;
                this._headersReceivedCapability.reject(n);
                this._requests.forEach(function(n) {
                    n.resolve({
                        value: undefined,
                        done: !0
                    })
                });
                this._requests = [];
                this._manager.isPendingRequest(this._fullRequestId) && this._manager.abortRequest(this._fullRequestId);
                this._fullRequestReader = null
            }
        };
        a.prototype = {
            _close: function() {
                if (this.onClosed) this.onClosed(this)
            },
            _onDone: function(n) {
                var t = n.chunk,
                i;
                this._requests.length > 0 ? (i = this._requests.shift(), i.resolve({
                    value: t,
                    done: !1
                })) : this._queuedChunk = t;
                this._done = !0;
                this._requests.forEach(function(n) {
                    n.resolve({
                        value: undefined,
                        done: !0
                    })
                });
                this._requests = [];
                this._close()
            },
            _onProgress: function(n) {
                if (!this.isStreamingSupported && this.onProgress) this.onProgress({
                    loaded: n.loaded
                })
            },
            get isStreamingSupported() {
                return ! 1
            },
            read: function() {
                var t, n;
                return this._queuedChunk !== null ? (t = this._queuedChunk, this._queuedChunk = null, Promise.resolve({
                    value: t,
                    done: !1
                })) : this._done ? Promise.resolve({
                    value: undefined,
                    done: !0
                }) : (n = r.createPromiseCapability(), this._requests.push(n), n.promise)
            },
            cancel: function() {
                this._done = !0;
                this._requests.forEach(function(n) {
                    n.resolve({
                        value: undefined,
                        done: !0
                    })
                });
                this._requests = [];
                this._manager.isPendingRequest(this._requestId) && this._manager.abortRequest(this._requestId);
                this._close()
            }
        };
        t.PDFNetworkStream = c;
        t.NetworkManager = e
    }])
})