let form = document.getElementById('guest-form');

form.onsubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    let jsonRequestData = {};

    for (let [key, value] of formData) {
        jsonRequestData[key] = value;
    }

    fetch('/create-guest', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonRequestData)
    }).then(async (res) => {
        if (res.status === 200) {
            window.location.href = '/view-guests';
        } else {
            let error = document.getElementById("error");
            let error_text = document.getElementById("error_text");
            error.classList.remove('hidden');
            let error_message = await res.json();

            error_text.innerHTML = `${error_message.error}`;
        }
    })
};