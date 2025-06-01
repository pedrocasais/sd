document.addEventListener('DOMContentLoaded', function() {

    const carousel = document.querySelector('.carousel');
    const prevBtn = document.querySelector('.nav-prev');
    const nextBtn = document.querySelector('.nav-next');
    const articles = document.querySelectorAll('.carousel article');


    if (!carousel || !prevBtn || !nextBtn || articles.length === 0) return;

    const itemWidth = articles[0].offsetWidth + 20;

    nextBtn.addEventListener('click', function() {
        carousel.scrollBy({
            left: itemWidth,
            behavior: 'smooth'
        });
    });

    prevBtn.addEventListener('click', function() {
        carousel.scrollBy({
            left: -itemWidth,
            behavior: 'smooth'
        });
    });
});