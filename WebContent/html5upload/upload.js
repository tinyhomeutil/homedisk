/*xiaolingzi@xxling.com*/
(function () {
    //公开方法
    var html5Upload = {
        init: null
    };
    //默认配置
    html5Upload.configDefault = {
        selectButtonId: null,
        uploadButtonId: null,
        fileProgressContainerId: null,
        failWord: "失败",
        waitingWord: "等待",
        fileTypeInvalidatedWord: "格式不支持",
        fileSizeInvalidatedWord: "文件超出限制",
        repeatSubmitWord: "请不要重复提交",
        fileTypes: null,
        fileSize: null,
        callback: null,
        url:null
    };
    //初始化事件
    html5Upload.init = function () {
        new Upload();
    };
    //上传事件构造函数
    function Upload() {
        this.config = html5Upload.config;
        this.config.failWord = !this.config.failWord ? html5Upload.configDefault.failWord : this.config.failWord;
        this.config.waitingWord = !this.config.waitingWord ? html5Upload.configDefault.waitingWord : this.config.waitingWord;
        this.config.repeatSubmitWord = !this.config.repeatSubmitWord ? html5Upload.configDefault.repeatSubmitWord : this.config.repeatSubmitWord;
        this.config.fileTypeInvalidatedWord = !this.config.fileTypeInvalidatedWord ? html5Upload.configDefault.fileTypeInvalidatedWord : this.config.fileTypeInvalidatedWord;
        this.config.fileSizeInvalidatedWord = !this.config.fileSizeInvalidatedWord ? html5Upload.configDefault.fileSizeInvalidatedWord : this.config.fileSizeInvalidatedWord;
        this.successCount = 0;
        this.failCount = 0;
        this.createFileInput();
        //绑定选择按钮事件
        addEvent("mousemove", document.getElementById(this.config.selectButtonId), function (obj, event) {
            return function (event) {
                obj.selectButtonMoveEvent(event);
            };
        } (this));
        //绑定上传按钮事件
        addEvent("click", document.getElementById(this.config.uploadButtonId), function (obj) {
            return function () {
                if (obj.isUploaded) {
                    alert(obj.config.repeatSubmitWord);
                    return;
                }
                obj.isUploaded = true;
                obj.uploadFile();
            };
        } (this));
    }
    //创建文件输入框
    Upload.prototype.createFileInput = function () {
        var id = Math.random();
        var fileInputId = ("html5UploadBox" + id).replace("0.", "");
        var fileInput = document.createElement("input");
        fileInput.id = this.fileInputId;
        fileInput.type = "file";
        fileInput.multiple = true;
        fileInput.style.position = "absolute";
        if (fileInput.filters) {
            fileInput.filters.alpha.opactity = 0;
        }
        else {
            fileInput.style.opacity = 0;
        }
        document.body.appendChild(fileInput);
        this.fileInput = fileInput;
        addEvent("change", this.fileInput, function (obj) {
            return function () {
                //重置属性值
                obj.progressPercentage = null;
                obj.isUploaded = false;
                obj.successCount = 0;
                obj.failCount = 0;
                //由于批量上传是一个一个文件分别上传，每次都要访问指定jsp，
                //为了帮助服务器端有效的识别每一批次的文件，因此采用点击
                //选择按钮后的当前时间作为每一批次文件的标识，供服务端保
                //存上传文件以及遇到错误回滚使用。(mark by jack)
                obj.timestamp = Date.parse(new Date());
                //执行列表显示
                if (!obj.progressTimer) {
                    obj.displayProgress();
                }
            };
        } (this));
    };
    //文件选择按钮事件 将透明的文件输入框至于鼠标点击范围
    Upload.prototype.selectButtonMoveEvent = function (event) {
        var x = event.clientX + document.body.scrollLeft;
        var y = event.clientY + document.body.scrollTop;
        this.fileInput.style.top = y - 5 + "px";
        this.fileInput.style.left = x + 5 - this.fileInput.offsetWidth + "px";
    };
    //显示文件列表
    Upload.prototype.displayProgress = function (isNotRepeat) {
        var files = this.fileInput.files;
        var progressContainer = document.getElementById(this.config.fileProgressContainerId);
        var html;
        if (!this.listTemplate) {
            this.listTemplate = progressContainer.innerHTML;
        }
        if (!this.progressPercentage) {
            this.progressPercentage = new Array();
        }
        html = this.listTemplate;
        var dealHTML = "";
        for (var i = 0, len = files.length; i < len; i++) {
            if (!this.progressPercentage[i]) {
                this.progressPercentage[i] = this.config.waitingWord;
            }
            if (!checkFileType(this.config.fileTypes, files[i].name)) {
                this.progressPercentage[i] = this.config.fileTypeInvalidatedWord;
            }
            else if (!checkFileSize(this.config.fileSize, files[i].size)) {
                this.progressPercentage[i] = this.config.fileSizeInvalidatedWord;
            }
            var tempHTML = html.replace(/\{FileName\}/g, files[i].name);
            tempHTML = tempHTML.replace(/\{FileType\}/g, files[i].type);
            tempHTML = tempHTML.replace(/\{FileSize\}/g, getFileSizeString(files[i].size));
            tempHTML = tempHTML.replace(/\{UploadPercentage\}/g, this.progressPercentage[i]);
            dealHTML += tempHTML;
        }
        progressContainer.innerHTML = dealHTML;
        progressContainer.style.display = "";
        if (isNotRepeat) {
            return;
        }
        this.progressTimer = setTimeout(function (obj) {
            return function () {
                obj.displayProgress();
            };
        } (this)
        , 200);
    };
    //上传文件到服务器
    Upload.prototype.uploadFile = function (index) {
        var files = this.fileInput.files;
        var len = files.length;
        var fd = new FormData();
        var request = httpRequest();
        if (!index && index != 0) {
            index = 0;
        }
        //如果不是合法文件则跳过不上传
        if (this.progressPercentage[index] != this.config.waitingWord) {
            index++;
            this.failCount++;
            this.uploadFile(index);
        }
        else {
        	fd.append("resource_upload_timestamp",this.timestamp);//加上时间戳，对同一批次的上传文件进行标记
        	fd.append("m", 35);//此参数为系统中功能标记，一定要加上，否则无法成功上传
            fd.append("ling_file_name", files[index]);
            request.open("POST", this.config.url, true);
            request.onprogress = uploadProgressEvent(this, index);
            request.onreadystatechange = uploadSuccessEvent(this, index, request, len);
            request.onerror = uploadFailEvent(this, index);
            request.send(fd);
        }
    };
    
    //上传中的函数
    function uploadProgressEvent(obj, index) {
        return function (event) {
            obj.progressPercentage[index] = parseInt(event.loaded * 100 / event.total) + "%";
        };
    }
    //上成功函数
    function uploadSuccessEvent(obj, index, request, len) {
        return function () {
            if (request.readyState === 4 && request.status === 200) {
                if (request.responseText.trim() === "1") {
                    obj.progressPercentage[index] = "100%";
                    obj.successCount++;
                } else {
                    obj.progressPercentage[index] = obj.config.failWord;//mark
                    obj.failCount++;
                }
                index++;
                if (index > len - 1) {
                    //结束时清除列表更新定时执行器 然后执行最后一遍
                    clearTimeout(obj.progressTimer);
                    obj.progressTimer = null;
                    obj.displayProgress(true);
                    if (obj.config.callback) {
                        obj.config.callback(len, obj.successCount, obj.failCount);
                    }
                    return;
                }
                obj.uploadFile(index);
            };
        }
    }
    //上传失败函数
    function uploadFailEvent(obj, index) {
        return function () {
            obj.progressPercentage[index] = obj.config.failWord;
            index++;
            obj.uploadFile(index);
        };
    }
    //动态绑定事件
    function addEvent(eventType, element, fn) {
        if (element && eventType && fn) {
            if (window.addEventListener) {
                element.addEventListener(eventType, fn);
            }
            else {
                element.attachEvent("on" + eventType, fn);
            }
        }
    };
    //获取XMLHttpRequest
    function httpRequest() {
        try {
            return new XMLHttpRequest();
        }
        catch (e) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    //转换文件大小为k或者M表示
    function getFileSizeString(size) {
        if (!size && typeof (size) !== "number") {
            return "0k";
        }
        if (size > 1024 * 1024 * 1024) {
            return (size / (1024 * 1024 * 1024)).toFixed(2) + "G";
        }
        else if (size > 1024 * 1024) {
            return (size / (1024 * 1024)).toFixed(2) + "M";
        }
        else {
            return (size / 1024).toFixed(2) + "k";
        }
    }
    //检查文件类型是否合法
    function checkFileType(allowFileTypes, fileName) {
        if (!allowFileTypes) {
            return true;
        }
        if (!fileName || fileName.indexOf('.') < 1) {
            return false;
        }
        var extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length).toLowerCase();
        allowFileTypes = allowFileTypes.toLowerCase();
        if (allowFileTypes.indexOf("|" + extension + "|") > -1) {
            return true;
        }
        return false;
    }
    //检查文件大小是否合法
    function checkFileSize(allowSize, fileSize) {
        if (!allowSize || typeof (allowSize) != "number") {
            return true;
        }
        if (fileSize <= allowSize) {
            return true;
        }
        return false;
    }
    window.$ling = window.$ling || {};
    window.$ling.html5Upload = html5Upload;
})();