window.AuthUtils = {
    // 存储Token到localStorage
    setToken: function(token) {
        localStorage.setItem('auth_token', token);
        // 设置token时同时启动计时器
        //this.startInactivityTimer();
    },
    // 获取Token
    getToken: function() {
        return localStorage.getItem('auth_token');
    },
    // 删除Token
    removeToken: function() {
        localStorage.removeItem('auth_token');
        // 清除token时同时清除计时器
        //this.clearInactivityTimer();
    },
    // 检查是否已登录
    isLogin: function() {
        if(!this.getToken()){
            window.location.href = '/views/login.html';
        } else {
            // 已登录状态下启动计时器
            //this.startInactivityTimer();
        }
    },

    // 检查是否已登录（跳转逻辑）
    isIndex: function() {
        if(this.getToken()){
            window.location.href = '/views/index.html';
            // 已登录状态下启动计时器
            //this.startInactivityTimer();
        }else{
            window.location.href = '/views/login.html';
        }
    },

    // 计时器变量
    inactivityTimer: null,
    timeoutTime: 5 * 60 * 1000,

    // // 启动无操作计时器
    // startInactivityTimer: function() {
    //     // 先清除已有的计时器
    //     this.clearInactivityTimer();
    //
    //     // 设置新的计时器
    //     this.inactivityTimer = setTimeout(() => {
    //         this.removeToken();
    //         window.location.href = '/views/login.html';
    //         alert('由于长时间未操作，您已被自动登出');
    //     }, this.timeoutTime);
    //
    //     // 监听用户操作，重置计时器
    //     const resetTimer = () => {
    //         this.clearInactivityTimer();
    //         this.startInactivityTimer();
    //     };
    //
    //     // 绑定常见的用户交互事件
    //     window.addEventListener('click', resetTimer);
    //     window.addEventListener('mousemove', resetTimer);
    //     window.addEventListener('keypress', resetTimer);
    //     window.addEventListener('scroll', resetTimer);
    // },
    //
    // // 清除计时器
    // clearInactivityTimer: function() {
    //     if (this.inactivityTimer) {
    //         clearTimeout(this.inactivityTimer);
    //         this.inactivityTimer = null;
    //     }
    // }
};