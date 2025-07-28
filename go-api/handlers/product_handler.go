package handlers

import (
	"context"
	"log"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/rommelchocho/go-api/db"
	"github.com/rommelchocho/go-api/models"
)

func GetProducts(c *gin.Context) {
	ids := strings.Split(c.Query("ids"), ",")
	query := `SELECT product_id, name, description, price, active FROM products WHERE product_id = ANY($1)`

	rows, err := db.DB.Query(context.Background(), query, ids)
	if err != nil {
		log.Printf("Error fetching products: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "error querying products"})
		return
	}
	defer rows.Close()

	var products []models.Product
	for rows.Next() {
		var p models.Product
		err := rows.Scan(&p.ProductID, &p.Name, &p.Description, &p.Price)
		if err == nil {
			products = append(products, p)
		}
	}
	c.JSON(http.StatusOK, products)
}
