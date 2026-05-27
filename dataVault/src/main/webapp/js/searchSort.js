// 等待 DOM 完全加载后执行
document.addEventListener('DOMContentLoaded', function() {
    // 获取搜索输入框和分类项列表
    const searchInput = document.getElementById('keywordInput');



    // 监听输入事件（用户输入时触发）
    searchInput.addEventListener('blur', function() {
        this.parentElement.classList.remove('focused');
        // 获取输入的关键词（去除首尾空格并转为小写）
        const keyword = this.value.trim().toLowerCase();
        const sortItems = document.getElementById("allSortContent").querySelectorAll('.sort');
        // 遍历所有分类项
        sortItems.forEach(item => {
            // 获取分类项的文本内容（去除首尾空格并转为小写）
            const itemText = item.textContent.trim().toLowerCase();

            // 判断是否包含关键词
            if (itemText.includes(keyword)) {
                // 包含关键词：显示分类项
                item.style.display = 'flex';
            } else {
                // 不包含关键词：隐藏分类项
                item.style.display = 'none';
            }
        });

        searchInput.addEventListener("input",function (){
            if (searchInput.value.length === 0){
                // 遍历所有分类项
                sortItems.forEach(item => {
                    item.style.display = 'flex';
                });
            }
        })
    });
});

// 添加搜索框交互效果
const input = document.getElementById('keywordInput');

input.addEventListener('focus', function() {
    this.parentElement.classList.add('focused');
});
//
// input.addEventListener('blur', function() {
//     this.parentElement.classList.remove('focused');
// });