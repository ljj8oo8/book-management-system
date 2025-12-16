const PermissionUtils = {
    // 存储用户权限
    permissions: [],

    // 初始化权限
    init: async function() {
        try {
            const res = await window.ApiClient.get('http://127.0.0.1:8080/user/permissions');
            if (res.data.code === 200) {
                this.permissions = res.data.data;
            }
        } catch (err) {
            console.error('获取权限失败', err);
        }
    },

    // 检查是否有权限
    hasPermission: function(permCode) {
        return this.permissions.includes(permCode);
    }
};