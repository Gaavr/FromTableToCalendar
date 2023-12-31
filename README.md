# University Schedule

## Program Description

This application streamlines the management of university class schedules by integrating Google Sheets and Google Calendar through a user-friendly Telegram bot interface. It offers the following key features:

- Data Retrieval: The program fetches class schedule data from Google Sheets, ensuring the most up-to-date information is available.
- Data Processing: After retrieving the data, the application processes and structures it for easy management.
- Google Calendar Integration: It allows you to add class events directly to Google Calendar, keeping track of important dates and times.
- Telegram Bot Interface: Interaction with the application is made simple and convenient through a Telegram bot, providing a chat-based interface for users to retrieve schedules and manage calendar events effortlessly.

By automating these tasks, the application saves time, reduces scheduling errors, and enhances the overall experience for both students and administrators.


## Features

- Fetching class schedule data from Google Sheets.
- Data processing and structuring.
- Adding events to Google Calendar based on class schedules.
- Interaction with the program through a Telegram bot.

## Technologies and Libraries

- Java
- Spring Framework
- Google Sheets API
- Google Calendar API
- Telegram Bot API

## Setup

1. Create a Google Sheet containing the class schedule.
2. Set up a project in the [Google Cloud Console](https://console.cloud.google.com/), enable the Google Sheets API and Google Calendar API, and obtain the necessary API keys and credentials.
3. Configure access to Google Calendar using the obtained credentials.
4. Create a Telegram bot and obtain its API token.
5. Configure the `application.properties` file to store configuration data, such as access keys and Telegram bot settings.

## Running the Application

1. Clone the repository.
2. Build the project using Maven: `mvn clean install`.
3. Run the application

## Usage

1. Launch the application.
2. Link your Telegram account to the bot and start interacting with the bot.
3. Send bot commands to fetch schedule data and add events to the calendar.

## Example Bot Commands

![bot-buttons-screen.jpg](img%2Fbot-buttons-screen.jpg)
![swagger-screen.jpg](img%2Fswagger-screen.jpg)

## Author

https://www.linkedin.com/in/andrey--gavrilenko/