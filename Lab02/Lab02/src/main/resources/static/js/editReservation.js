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
        if (res.ok) {
            window.location.href = '/view-reservation';
        } else {
            let error = document.getElementById("error");
            let error_message = await res.json();
            error.innerHTML = `${error_message.error}`;
        }
    })
}
