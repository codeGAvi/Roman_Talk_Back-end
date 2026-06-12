---
title: Romantalk Backend
emoji: 🚀
colorFrom: pink
colorTo: purple
sdk: docker
app_port: 8080
pinned: false
license: apache-2.0
short_description: AI-powered service that translates 20+ languages into Roman script
---

# RomanTalk Backend

Spring Boot backend for RomanTalk.

## Features

- Translate 20+ languages into Roman script
- Gemini AI integration
- PostgreSQL (Neon)
- Redis (Upstash)
- Razorpay payments
- JWT Authentication

## Environment Variables

Required secrets:

- DB_PASSWORD
- GOOGLE_GENAI_API_KEY
- REDIS_PASSWORD
- RAZORPAY_KEY_ID
- RAZORPAY_KEY_SECRET
- JWT_SECRET

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring AI
- PostgreSQL
- Redis
- Docker

## API Status

Backend service running on port 8080.