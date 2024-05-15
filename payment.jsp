<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <!-- Include Razorpay library -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <style>
        #PaymentType {
            margin-top: 10px;
            margin-left: 120px;
            margin-bottom: 300px;
            margin-right: 90px;
            border: 1px solid #ccc;
            padding-top: 50px;
            padding-bottom: 60px;
            padding-left: 100px;
        }

        body {
            font-family: Arial, sans-serif;
            background-image: linear-gradient(to right, #ffbb33, #ff6f91);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
    </style>
</head>
<body>
    <form method="GET" id="PaymentType">
        <label><b>Mode Of Payment:</b></label>
        <select class="btn btn-outline-dark" style="margin-right:40px;" name="category" id="PaymentSelect">
            <option value="">Choose Payment Type</option>
            <option value="cod">Cash On Delivery</option>
            <option value="razorpay">Online Payment</option>
        </select>
    </form>

    <script>
        function pay() {
            var grandTotalAfterCoupon =5073 ;

            // Convert to integer paise
            var options = {
                "key": "rzp_test_8zWJzVULM9vye4", // Enter the Key ID generated from the Dashboard
                "amount": Math.round(grandTotalAfterCoupon * 100),
                "currency": "INR",
                "description": "Acme Corp",
                "image": "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg",
                "prefill": {
                    "email": "manishareddy@gmail.com",
                    "contact": +917735422318,
                },
                "config": {
                    "display": {
                        "blocks": {
                            "utib": {
                                "name": "Pay using Axis Bank",
                                "instruments": [
                                    {
                                        "method": "card",
                                        "issuers": ["UTIB"]
                                    },
                                    {
                                        "method": "netbanking",
                                        "banks": ["UTIB"]
                                    },
                                ]
                            },
                            "other": {
                                "name": "Other Payment modes",
                                "instruments": [
                                    {
                                        "method": "card",
                                        "issuers": ["ICIC"]
                                    },
                                    {
                                        "method": "netbanking",
                                    }
                                ]
                            }
                        },
                        "hide": [
                            {
                                "method": "upi"
                            }
                        ],
                        "sequence": ["block.utib", "block.other"],
                        "preferences": {
                            "show_default_blocks": false
                        }
                    }
                },
                "handler": function (response) {
                    alert(response.razorpay_payment_id);
                },
                "modal": {
                    "ondismiss": function () {
                        if (confirm("Are you sure, you want to close the form?")) {
                            console.log("Checkout form closed by the user");
                        } else {
                            console.log("Complete the Payment");
                        }
                    }
                }
            };

            var rzp1 = new Razorpay(options);
            console.log(options);
            rzp1.open();
        }
        
        document.getElementById('PaymentSelect').addEventListener('change', function() {
            var selectedOption = this.value;
            if (selectedOption === 'razorpay') {
                pay(); // Call the pay function when Razorpay is selected
            }
        });
    </script>
</body>
</html>
