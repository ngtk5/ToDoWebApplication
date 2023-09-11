"use strict";

{
    // ページが読み込まれた後に実行する処理
    window.addEventListener('DOMContentLoaded', () => {
        const notification = document.getElementById('notification');
        // 通知メッセージを表示する
        if (notification != null) {
            notification.innerText = alertMessage;
            notification.style.display = "block";
            setTimeout(() => {
                notification.style.display = "none";
            }, 5000);  // 5000ミリ秒（5秒）後に非表示にする
        }
    })
}