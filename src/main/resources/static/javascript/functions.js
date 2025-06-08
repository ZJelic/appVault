function openDeleteConfirmModal(element) {
    const id = element.getAttribute("data-id");
    const deleteUrl = element.getAttribute("data-delete-url");
    const itemName = element.getAttribute("data-item-name") || "this item";

    const form = document.getElementById("deleteForm");
    form.setAttribute("action", deleteUrl);

    const message = document.getElementById("deleteModalMessage");
    message.textContent = `Are you sure you want to delete ${itemName}?`;

    document.getElementById("deleteModal").classList.remove("hidden");
}

function closeDeleteConfirmModal() {
    document.getElementById("deleteModal").classList.add("hidden");
}

// Custom handling validation
document.addEventListener('DOMContentLoaded', () => {

    const forms = document.querySelectorAll('form');
    document.addEventListener('click', () => {
        const errorMessages = document.querySelectorAll('.validation-message.show');
        errorMessages.forEach(msg => msg.classList.remove('show'));
    });
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input[required]');
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            let valid = true;

            inputs.forEach(input => {
                const wrapper = input.closest('.input-wrapper');
                const errorMsg = wrapper.querySelector('.validation-message');
                errorMsg.classList.remove('show');

                if (!input.value.trim()) {
                    errorMsg.textContent = `${input.name.charAt(0).toUpperCase() + input.name.slice(1)} is required`;
                    errorMsg.classList.add('show');
                    valid = false;
                } else if (input.type === 'email' && !validateEmail(input.value)) {
                    errorMsg.textContent = 'Please enter a valid email';
                    errorMsg.classList.add('show');
                    valid = false;
                }
            });

            if (valid) {
                form.submit();
            }
        });

        inputs.forEach(input => {
            const wrapper = input.closest('.input-wrapper');
            const errorMsg = wrapper.querySelector('.validation-message');
            input.addEventListener('input', () => {
                errorMsg.classList.remove('show');
            });
        });
    });

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }
});