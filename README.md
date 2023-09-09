# University Schedule

## Program Description

This Java application, built with Spring, automates the process of fetching class schedule data from Google Sheets, processing it, and adding it to Google Calendar. The program is controlled via a Telegram bot.

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

![img.png](readme/img.png)

## Author

https://www.linkedin.com/in/andrey--gavrilenko/
gavrjob@gmail.com
https://t.me/gaavr

[Specify your name and contact information]

