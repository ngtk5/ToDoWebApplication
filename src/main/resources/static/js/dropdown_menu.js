"use strict";

{
    // ページが読み込まれた後に実行する処理
    window.addEventListener('DOMContentLoaded', () => {
        // DOM取得
        // 親メニューの要素
        const DropDownMenuParent = document.getElementById('dropdown-user-menu-parent');
        console.log("parentMenuItem:"+DropDownMenuParent);
        DropDownMenuParent.addEventListener("mouseover", () => {
            dropDownMenuOpen();
        },false);
        DropDownMenuParent.addEventListener("mouseout", () => {
            dropDownMenuClose();
          },false);
        })
        // ドロップダウンメニューを開く処理
        const dropDownMenuOpen = () => {
            const dropDownMenuChildren = document.getElementById("dropdown-user-menu-children");
            dropDownMenuChildren.classList.add("show");
            dropDownMenuChildren.setAttribute('data-bs-popper', "none");
            const userMenu = document.getElementById("user-menu");
            userMenu.classList.add("show");
            userMenu.setAttribute('aria-expanded', true);
        }

        // ドロップダウンメニューを閉じる処理
        const dropDownMenuClose = () => {
            const dropDownMenuChildren = document.getElementById("dropdown-user-menu-children");
            dropDownMenuChildren.classList.remove("show");
            dropDownMenuChildren.removeAttribute('data-bs-popper');
            const userMenu = document.getElementById("user-menu");
            userMenu.classList.remove("show");
            userMenu.setAttribute('aria-expanded', false);
            userMenu.removeAttribute('data-bs-popper');
        }
}