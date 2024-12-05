# RealTime-Ticketing-System
Real-Time Event Ticketing System using OOP principles

-- For temporary Running (Backend Only) -- 

GET http://localhost:8080/api/config

POST http://localhost:8080/api/config

{
    "totalTickets": 500,
    "ticketReleaseRate": 10,
    "customerRetrievalRate": 8,
    "maxTicketCapacity": 50
}

POST http://localhost:8080/api/config/start

POST http://localhost:8080/api/config/stop
