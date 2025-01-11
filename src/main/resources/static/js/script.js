document.addEventListener('DOMContentLoaded', () => {
    const estimates = document.querySelectorAll('.order');

    estimates.forEach(order => {
        order.addEventListener('wheel', (e) => {
            if (e.deltaY !== 0) {
                e.preventDefault();
                order.scrollBy({
                    top: e.deltaY, // Прокрутка по вертикали
                    behavior: 'smooth'
                });
            }
        });
    });
});