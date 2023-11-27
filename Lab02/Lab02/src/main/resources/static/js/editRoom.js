let form = document.getElementById('room-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    fetch('/edit-room', {
        method: 'POST',
        body:  new FormData(form)
    }).then(async (res) => {
        let error_messages = await res.json();
        if (!Array.isArray(error_messages) || error_messages.length === 0) {
            window.location.href = '/view-rooms';
        } else {
            let error_text = document.getElementById("error");
            error_messages.forEach(message => {
                error_text.innerHTML += `${message}<br/>`;
            })
        }
    })
};