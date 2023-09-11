"use strict";
// ceate_task.htmlのみで使用する関数を記述するjsファイル
{
    // 日付をYYYY-MM-DDの書式で返すメソッド
    var formatDate = (dt) => {
        var y = dt.getFullYear();
        var m = ('00' + (dt.getMonth()+1)).slice(-2);
        var d = ('00' + dt.getDate()).slice(-2);
        return (y + '-' + m + '-' + d);
    }

    // ページをロードしたときに行なう処理
    window.addEventListener('DOMContentLoaded', ()=>{
        const deadline = document.getElementById("deadline");
        // 過去の日付を選択できないように
        deadline.min = formatDate(new Date());
    })
}