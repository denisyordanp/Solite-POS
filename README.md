# Solite-POS
Android kotlin POS System project for my own business at Jajanan Sosialita store

<a href="https://play.google.com/store/apps/details?id=com.socialite.solite_pos">Google play</a> | <a href="https://www.instagram.com/jajanansosialita/">Instagram</a> | <a href="https://www.facebook.com/jajanansosialita">Facebook</a>

> Dont forget to visit this app on <a href="https://play.google.com/store/apps/details?id=com.socialite.solite_pos">Google play</a> to see the beautiful UI Screenshoot on this

# Project status
## Health
| Build                                                                                                                                                                                              | Code Smells                                                                                                                                                    | Test Coverage                                                                                                                                            |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![CircleCI](https://dl.circleci.com/status-badge/img/gh/denisyordanp/Solite-POS/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/denisyordanp/Solite-POS/tree/master) | [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=solite_pos&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=solite_pos) | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=solite_pos&metric=coverage)](https://sonarcloud.io/summary/new_code?id=solite_pos) |
## Structure
- MVVM Design Pattern
- Clean Architecture

# Feature
1. Login 
   1. User able to login with registered account.
2. Register 
   1. User able to register new account along with main store name.
3. Offline Process capability
   1. Technically all the data are **Queried, Stored and Process Locally** in the android native machine. (_No need internet connection_)
   2. In the other hands, User also able to **Backup** the data to server by manually. (_See number 9._)
4. Order Process
   1. On progress
      - Orders will have on progress status when it **First ordered**
   2. Need Pay
      - After the order **Finished**, user need to **Manually** put the order status to **Finished** then the status will move to **Need pay** status
   3. Canceled
      - Orders **Can** be cancel when the orders still **On progress** or **Need Pay** while not for the status **Done**
   4. Done
      - When orders status on the **On progress** or **Need pay**, user need to do a **Payment** so the orders will moved to **Done** status
5. Order History
   1. User able see the orders history with **All the order process status** by range of date.
6. Sales Report / Recap
   1. User able see the sales repost selected by **Date range** and **Store branch**.
7. Master Data
    - Store
    - Payment
    - Outcome
    - Promo
    - Category Product
    - Product
    - Variant Product
    - Customer
8. Bluetooth Print Connection
   1. User able to print bill of orders
   2. User able to print orders queue within the day. (_Ordered by autoincrement order number_)
9. Online Backup
   1. User able to backup their data to server so it can be available on multiple **Logged in devices**.
10. Multiple Branch Store
    1. User able to have more than one store branches on the main store.
    2. Every store branch will have their own **Recap** data and **Orders**

# Technical
### Library
- Firebase 
  - Crashlytics (_Monitor crash_)
  - Remote Config (_Setup server online status_)
- Jetpack
  - Dagger Hilt (_Dependency injections_)
  - Data Store Preferences (_Store user token_)
  - Shared Preferences (_Store user settings_)
  - Compose (_UI system_)
  - Compose Navigation (_Screen navigation system_)
  - Accompanist Pager (_Screen tab_)
- Retrofit (_Network connections_)
- Room (_Database system_)
- In App Update (_Emergency force update_)
- Easy Permission (_Bluetooth permission_)


### License
`This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License. See [LICENSE](LICENSE.md) for details.`
