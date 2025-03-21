const apiUrl = "http://localhost:8080/api/exchange";

// Функция загрузки курса валют
async function fetchRates() {
    try {
        const response = await fetch(`${apiUrl}/rate`);
        if (!response.ok) throw new Error("Ошибка запроса");

        const data = await response.json();
        console.log("Курсы валют:", data); // Посмотреть, что пришло от сервера

        document.getElementById("usd-rate").textContent = data.USD + " грн";
        document.getElementById("eur-rate").textContent = data.EUR + " грн";
    } catch (error) {
        console.error("Ошибка получения данных:", error);
        document.getElementById("usd-rate").textContent = "Ошибка";
        document.getElementById("eur-rate").textContent = "Ошибка";
    }
}

// Функция конвертации валюты
async function convertCurrency() {
    const amount = document.getElementById("amount").value;
    const currency = document.getElementById("currency").value;

    try {
        const response = await fetch(`${apiUrl}/convert?amount=${amount}&currency=${currency}`);
        if (!response.ok) throw new Error("Ошибка конвертации");

        const convertedAmount = await response.json();
        document.getElementById("result").textContent = convertedAmount + " грн";
    } catch (error) {
        console.error("Ошибка конвертации:", error);
        document.getElementById("result").textContent = "Ошибка";
    }
}

async function downloadExcel() {
    const amount = document.getElementById("amount").value;
    const currency = document.getElementById("currency").value;

    if (!amount) {
        alert("Введіть суму для конвертації!");
        return;
    }

    const url = `http://localhost:8080/api/exchange/download-excel?amount=${amount}&currency=${currency}`;

    fetch(url)
        .then(response => response.blob())
        .then(blob => {
            const link = document.createElement("a");
            link.href = window.URL.createObjectURL(blob);
            link.download = "transaction.xlsx";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        })
        .catch(error => console.error("Помилка скачування:", error));
}


// Загружаем курсы валют при старте
fetchRates();
