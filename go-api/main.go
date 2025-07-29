package main

import (
	"log"

	"github.com/gin-gonic/gin"
	"github.com/rommelchocho/go-api/db"
	"github.com/rommelchocho/go-api/handlers"
)

func main() {
	log.Println("Iniciando Go API...")
	log.Println("Conectando a la DB...")
	if err := db.Connect(); err != nil {
		log.Fatalf("Error al conectar a la DB: %v", err)
	}

	r := gin.Default()

	api := r.Group("/api")
	{
		api.GET("/clientes/:id", handlers.GetCustomer)
		api.GET("/productos", handlers.GetProducts)
	}

	r.Run(":8083")
}
