package handlers

import (
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/rommelchocho/go-api/db"
	"github.com/rommelchocho/go-api/models"
)

func GetCustomer(c *gin.Context) {
	id := c.Param("id")
	var customer models.Customer

	err := db.DB.QueryRow(context.Background(),
		`SELECT customer_id, name, email, active FROM customers WHERE customer_id=$1`,
		id,
	).Scan(&customer.CustomerID, &customer.Name, &customer.Email, &customer.Active)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "customer not found"})
		return
	}

	c.JSON(http.StatusOK, customer)
}
