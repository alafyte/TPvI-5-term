let form = document.getElementById('room-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    fetch('/edit-room', {
        method: 'POST',
        body:  new FormData(form)
    }).then(async (res) => {
        if (res.status === 200) {
            window.location.href = '/view-rooms';
        } else {
            let error = document.getElementById("error");
            let error_text = document.getElementById("error_text");
            error.classList.remove('hidden');
            let error_message = await res.json();

            error_text.innerHTML = `${error_message.error}`;
        }
    })
};