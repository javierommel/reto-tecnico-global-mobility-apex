package main

import (
	"log"

	"github.com/gin-gonic/gin"
	"github.com/rommelchocho/go-api/db"
	"github.com/rommelchocho/go-api/handlers"
)

func main() {
	if err := db.Connect(); err != nil {
		log.Fatalf("❌ Failed to connect to DB: %v", err)
	}

	r := gin.Default()

	api := r.Group("/api")
	{
		api.GET("/clientes/:id", handlers.GetCustomer)
		api.GET("/productos", handlers.GetProducts)
	}

	r.Run(":8083")
}
