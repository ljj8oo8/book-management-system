
window.validate = {
    isbnValid: function(isbn) {
        const isbnStr = String(isbn || '').trim();
        if (!isbnStr || isbnStr.trim() === '') {
            return {valid: false, message: 'ISBN不能为空'};
        }

        // 移除所有连字符，统一处理
        const cleanedISBN = isbnStr.replace(/-/g, '').trim();

        // 检查长度是否为10位或13位
        if (![10, 13].includes(cleanedISBN.length)) {
            return {valid: false, message: 'ISBN长度必须为10位或13位（去除连字符后）'};
        }

        // 10位ISBN校验
        if (cleanedISBN.length === 10) {
            // 前9位必须为数字，最后一位可以是数字或X（大写）
            if (!/^[0-9]{9}[0-9X]$/.test(cleanedISBN)) {
                return {valid: false, message: '10位ISBN格式错误，前9位为数字，最后一位可为数字或X'};
            }

            // 校验位计算
            let sum = 0;
            for (let i = 0; i < 9; i++) {
                sum += parseInt(cleanedISBN[i], 10) * (10 - i);
            }
            const checkDigit = 11 - (sum % 11);
            const actualCheckDigit = cleanedISBN[9] === 'X' ? 10 : parseInt(cleanedISBN[9], 10);

            // 特殊情况：校验位为11时，实际应为0
            if (!(checkDigit === actualCheckDigit || (checkDigit === 11 && actualCheckDigit === 0))) {
                return {valid: false, message: '10位ISBN校验位不正确'};
            }
        }

        // 13位ISBN校验（EAN-13标准）
        if (cleanedISBN.length === 13) {
            // 必须全为数字
            if (!/^[0-9]{13}$/.test(cleanedISBN)) {
                return {valid: false, message: '13位ISBN必须全为数字'};
            }

            // 校验位计算
            let sum = 0;
            for (let i = 0; i < 12; i++) {
                const digit = parseInt(cleanedISBN[i], 10);
                sum += i % 2 === 0 ? digit * 1 : digit * 3; // 奇数位乘1，偶数位乘3
            }
            const checkDigit = (10 - (sum % 10)) % 10; // 取模10后的补数
            if (checkDigit !== parseInt(cleanedISBN[12], 10)) {
                return {valid: false, message: '13位ISBN校验位不正确'};
            }
        }

        // 校验通过
        return {valid: true, message: ''};
    }

};