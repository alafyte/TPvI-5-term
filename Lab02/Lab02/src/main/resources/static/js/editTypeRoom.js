let form = document.getElementById('type-room-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    let jsonRequestData = {};

    for (let [key, value] of formData) {
        jsonRequestData[key] = value;
    }
    fetch('/edit-type-room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonRequestData)
    }).then(async (res) => {
        let error_messages = await res.json();
        if (error_messages.length === 0) {
            window.location.href = '/view-type-rooms';
        } else {
            let error_text = document.getElementById("error");
            error_messages.forEach(message => {
                error_text.innerHTML += `${message}<br/>`;
            })
        }
    })
}
