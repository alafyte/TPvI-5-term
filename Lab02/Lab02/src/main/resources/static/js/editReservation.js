let form = document.getElementById('reservation-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    let jsonRequestData = {};

    for (let [key, value] of formData) {
        jsonRequestData[key] = value;
    }
    fetch('/edit-reservation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonRequestData)
    }).then(async (res) => {
        let error_messages = await res.json();
        if (error_messages.length === 0) {
            window.location.href = '/view-reservation';
        } else {
            let error_text = document.getElementById("error");
            error_messages.forEach(message => {
                error_text.innerHTML += `${message}<br/>`;
            })
        }
    })
}
