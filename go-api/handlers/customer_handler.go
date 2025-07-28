package handlers

import (
	"context"
	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/rommelchocho/go-api/db"
	"github.com/rommelchocho/go-api/models"
)

func GetCustomer(c *gin.Context) {
	id := c.Param("id")
	var customer models.Customer
	log.Println("Fetching customer with ID:", id)
	err := db.DB.QueryRow(context.Background(),
		`SELECT customer_id, name, email, phone, active FROM customers WHERE customer_id=$1`,
		id,
	).Scan(&customer.CustomerID, &customer.Name, &customer.Email, &customer.Phone, &customer.Active)

	if err != nil {
		log.Printf("Error fetching customer: %v", err)
		c.JSON(http.StatusNotFound, gin.H{"error": "customer not found"})
		return
	}
	log.Println("Customer fetched successfully:", customer)
	c.JSON(http.StatusOK, customer)
}
