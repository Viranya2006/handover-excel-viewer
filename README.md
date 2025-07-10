# HandOver Excel Viewer — Android App

An Android app that allows users to upload Microsoft Excel (.xlsx) files and search for customer records by either ACCT\_NUM or ID. It displays a formatted view of only selected fields, with correct alignment even if some cells in Excel are blank.

## ✨ Features

* 📂 Upload and parse .xlsx files using Apache POI
* 🔍 Search records by either ACCT\_NUM or ID
* ✔️ Displays only selected, relevant fields
* 📅 Auto-formats date cells (yyyy-MM-dd)
* ⚪ Handles empty cells correctly (displays "–")
* 🌞 Always uses light theme (ignores dark mode)

## 🔣 Displayed Fields

* ID
* ACCT NUM
* MET NUM
* WALK ORDER
* NAME
* ADDRESS
* BILL CYCLE
* TARIFF COD
* DATE
* COM
* ROUTE
* TP

## ⚙️ Tech Stack

* Java (Android)
* Apache POI (Excel file parser)
* Material Design 3 (Material3)
* RecyclerView
* View Binding
* Min SDK: 25
* Target SDK: 35

## 📦 Project Structure

```
/app
  /src
    /main
      /java/com/luxevista/handover
        MainActivity.java
        ResultAdapter.java
      /res/layout
        activity_main.xml
        item_row.xml
      /res/values
        strings.xml
  build.gradle (Module)
README.md
.gitignore
```

## 📋 How to Use

1. Clone this repo or open the project in Android Studio
2. Install dependencies and build the project
3. Run the app on a device or emulator
4. Click "Upload Excel" and choose a .xlsx file
5. Use the dropdown to select search by ACCT\_NUM or ID
6. Enter the value and press "Search"

## ⚖️ License

This project is open source and available under the MIT License or Apache 2.0 License.

## 🚀 Future Improvements

* Export search results as PDF/CSV
* More searchable fields (e.g. NAME, ROUTE)
* Improved error UI for invalid/malformed Excel
* Offline caching of uploaded files

---

Made with ❤️ by your team.
