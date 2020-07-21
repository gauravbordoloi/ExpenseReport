# Expense Report

[![N|Solid](https://www.gauravbordoloi.me/wp-content/uploads/2020/06/gb-landscape.png)](https://www.gauravbordoloi.me)

# Expense Report is an android application to:

  - View all expenses and income as list or graph (pie and bar) .
  - Set tags to various expense/income.
  - View all tags with expense/income in a bar graph.
  - Download report of any graph as an image to local storage.
  - Filter daily, monthly transactions.

# Popular Libraries Uses:

  - MPAndroidChart [For Pie and Bar chart].
  - Mindorks Screenshot [For graph image].
  - Jetpack Room [For local android DB].
  - Timber [For logging].
  - Anko [Background task].


# Top Key Points:
  - Single activity application.
  - Completely uses Android Navigation Components from jetpack.
  - Uses latest Material widgets and AndroidX components.
  - Uses vector drawables and webp icons

# Possible bugs:
  - MPAndroidChart Bar Garph Crash Issue : Resetting the graph/chart crashes the application as there is no proper way to reset it. Reproduce this issue by changing the graph from "Daily" to "Monthly" and vice versa multiple times.
  - MPAndroidChart Bar Graph UI Issue : Sometimes the bar graph, depending on the transactional data shows bars with unaligned labels on X-axis. This happens if on some day or month, either of Credit or Expense is empty.
  - Lot of Non-Transaction messages shows up as transactional as the message parse is not well trained/established. A better solution is given below in the "Improvements" section.

# Possible solutions to above bugs:
  - MPAndroidChart Bar Garph Crash Issue : Need to raise an issue in github.
  - MPAndroidChart Bar Graph UI Issue : Need to calculate the bar and groups width complex data samples but may produce unexpected results. It is better to raise an issue in github.

# Improvements:
  - Sort transactional SMS based on Sender ID rather than body text. Keeping a record of all transactional Sender ID is not a one day solution. It takes lots of days to collect and filter the Sender IDs. So, the best approach is to use "Truecaller" APIs to get the Sender IDs. Truecaller is in this field for years and have created a huge database of various Sender IDs which will be very useful.

Application Configurations [Knowledgebase]

> Packagae Name - me.gauravbordoloi.expensereport
> Permissions - READ_SMS, WRITE_EXTERNAL_STORAGE
> Minimum SDK 21
> Target SDK 29
> Version Code 1
> Version Name 1.0

Android Studio Configurations [To import this project]

> Android Studio 4.0
> Kotlin Version 1.3.72
> Build Tools 30.0.0
> Gradle 6.1.1