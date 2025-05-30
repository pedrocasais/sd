const menu = document.querySelector(".menu");

const offScreenMenu = document.querySelector(".menu-off");

menu.addEventListener("click", () => {
    menu.classList.toggle("active");
    offScreenMenu.classList.toggle("active");
});