"use strict";

{
    // 状態切り替えボタンをクリックしたときの処理
    var setIsCompletedFlg = () => {
        var iscompletedFlg = document.getElementById('completedFlg').value;
        var taskStatus = document.getElementById('taskStatus').innerHTML;
        // isCompletedFlg=trueまたはtaskStatus=完了のとき
        if (iscompletedFlg == true || taskStatus == '完了') {
            document.getElementById('taskStatus').innerHTML = '未完了';
            document.getElementById('completedFlg').value = false;
        // isCompletedFlg=falseまたはtaskStatus=未完了のとき
        } else if (iscompletedFlg == false || taskStatus == '未完了') {
            document.getElementById('taskStatus').innerHTML = '完了';
            document.getElementById('completedFlg').value = true;
        // 想定外の値が格納されているとき
        } else {
            document.getElementById('taskStatus').innerHTML = '未完了';
            document.getElementById('completedFlg').value = false;
        }
    }
    // 削除ボタンの関数
    var deleteBtn = (event) => {
        var result = confirm("削除してもよろしいですか？");
        if (!result) {
            event.preventDefault(); // リンクのデフォルトの遷移をキャンセル
            console.log("削除処理キャンセル");
        }
    };
}