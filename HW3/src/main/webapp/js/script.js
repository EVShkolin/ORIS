document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search)
    const nameError = urlParams.get("nameError")
    const emailError = urlParams.get("emailError")
    const nameErrorParagraph = document.getElementById("name-error")
    const emailErrorParagraph = document.getElementById("email-error")

    if (nameError) {
        nameErrorParagraph.textContent = nameError
        nameErrorParagraph.classList.remove("hidden")
    }

    if (emailError) {
        emailErrorParagraph.textContent = emailError
        emailErrorParagraph.classList.remove("hidden")
    }
})