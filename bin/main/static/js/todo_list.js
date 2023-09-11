"use strict";

{
    // 削除ボタンの関数
    var deleteBtn = (event) => {
        var result = confirm("削除してもよろしいですか？");
        if (!result) {
            event.preventDefault(); // リンクのデフォルトの遷移をキャンセル
            console.log("削除処理キャンセル");
        }
    };

    // 完了済みタスクを全て削除するボタンの関数
    var allDeleteBtn = (event) => {
        var result = confirm("完了済みタスクがすべて削除されます。\nよろしいですか？");
        if (!result) {
            event.preventDefault(); // リンクのデフォルトの遷移をキャンセル
            console.log("削除処理キャンセル");
        }
    };
}