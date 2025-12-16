if (typeof axios === 'undefined') {
    throw new Error('请先引入axios！');
}

// 全局对象ApiClient，替代export/import
window.ApiClient = (function() {
    // 创建axios实例
    const api = axios.create({
        baseURL: 'http://127.0.0.1:8080',
        timeout: 5000
    });

    // 请求拦截器：添加Token
    api.interceptors.request.use(
        function(config) {
            const token = AuthUtils.getToken();
            if (token) {
                config.headers['Authorization'] = `Bearer `+encodeURIComponent(token);
            }
            return config;
        },
        function(error) {
            return Promise.reject(error);
        }
    );

    // 响应拦截器：处理Token过期
    api.interceptors.response.use(
        function(response) {
            return response;
        },
        function(error) {
            if (error.response && error.response.status === 401) {
                // Token过期或无效，强制登出
                AuthUtils.removeToken();
                window.location.href = '/views/login.html';
                alert('登录已过期，请重新登录');
            }
            return Promise.reject(error);
        }
    );

    // 暴露常用请求方法（简化调用）
    return {
        get: function(url, params) {
            return api.get(url, { params });
        },
        post: function(url, data) {
            return api.post(url, data);
        },
        put: function(url, data) {
            return api.put(url, data);
        },
        delete: function(url) {
            return api.delete(url);
        }
    };
})();