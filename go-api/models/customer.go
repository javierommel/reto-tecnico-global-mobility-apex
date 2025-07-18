package models

type Customer struct {
	CustomerID string `json:"customerId"`
	Name       string `json:"name"`
	Email      string `json:"email"`
	Active     bool   `json:"active"`
}
