let form = document.getElementById('register-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    let jsonRequestData = {};

    for (let [key, value] of formData) {
        jsonRequestData[key] = value;
    }
    console.log(jsonRequestData);
    fetch('/register-worker', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonRequestData)
    }).then(async (res) => {
        if (res.ok) {
            window.location.href = '/view-workers';
        } else {
            let error = document.getElementById("error");
            let error_message = await res.json();
            error.innerHTML = `${error_message.error}`;
        }
    })
}
