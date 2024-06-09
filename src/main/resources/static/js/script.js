// const paymentStart = () => {
//     console.log("payment started...");
//     let amount = $("#payment_field").val();
//     console.log(amount);
//     if (amount === '' || amount === null) {
//         alert("Please enter any amount");
//         return;
//     }

//     $.ajax({
//         url: '/user/create_order',
//         data: JSON.stringify({ amount: amount, info: 'order_request' }),
//         contentType: 'application/json',
//         type: 'POST',
//         dataType: 'json',
//         success: function (response) {
//             // invoked when successful
//             console.log(response);
//             if (response.id) {  // Assuming 'response.id' indicates success
//                 //    m =  open payment form
//                 let options = {
//                     key: 'rzp_test_qo8SLCpZJLXOIZ',
//                     amount: response.amount,
//                     currency: 'INR',
//                     name: 'Smart Contact Manager',
//                     description: 'Support',
//                     image: "https://i.pinimg.com/564x/9b/d8/8c/9bd88c8e74d666fed60487f95f419c12.jpg",
//                     order_id: response.id,
//                     handler: function (response) {
//                         console.log(response.razorpay_payment_id);
//                         console.log(response.razorpay_order_id);
//                         console.log(response.razorpay_signature);
//                         alert('Congratulations, Payment Successful!');
//                     },
//                     prefill: {
//                         name: "Gaurav Kumar",
//                         email: "gaurav.kumar@example.com",
//                         contact: "9999999999"
//                     },
//                     notes: {
//                         address: "Mustafvi for All"
//                     },
//                     theme: {
//                         color: "#3399cc"
//                     }
//                 };
//                 let rzp = new Razorpay(options);
//                 rzp.on('payment.failed', function (response) {
//                     console.log(response.error.code);
//                     console.log(response.error.description);
//                     console.log(response.error.source);
//                     console.log(response.error.step);
//                     console.log(response.error.reason);
//                     console.log(response.error.metadata.order_id);
//                     console.log(response.error.metadata.payment_id);
//                     alert('Oops, Payment Failed!');
//                 });
//                 rzp.open(); // Open the Razorpay checkout
//             }
//         },
//         error: function (error) {
//             // invoked when error
//             console.log(error);
//             alert("Something went wrong!");
//         }
//     });
// };
