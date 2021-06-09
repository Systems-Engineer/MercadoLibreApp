## Mercado Libre app

# Arquitectura
Esta app fue desarrollada utilizando arquitectura MVI para implementar el manejo de estados.
Por lo que posee 3 directorios MVI y 1 para injection de dependencias:
- di
- model
- view
- viewmodel

# Patrones de diseño
Esta app implementa patrones como el Factory, Singleton, Adapter y Observer principalmente.

# Testing
Esta app implementa pruebas unitarias y pruebas de instrumentation automatizadas, usando tecnologias como:
- Espresso
- AndroidX.Test
- MockK
- MockWebServer

# Injeccion de Dependencias
Esta app implementa injeccion de dependencias a traves de Hilt/Dagger facilitando la implementacion de patrones como factory y singleton, ademas de ahorrar mucho codigo boilerplate.

# Manejo de memoria
Esta app implementa el manejo de memoria a traves del uso de coroutines las cuales son lifecycle aware y por lo tanto se liberan de la memoria una vez el ViewModel es destruido gracias al viewModelScope.

# UI/UX
Esta app tiene su diseño basado en la aplicacion oficial de MercadoLibre disponible en la Google Play Store Colombia.