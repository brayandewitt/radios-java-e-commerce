// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {

    const popup = Notification();
    popup.setProperty({
        duration: 5000,
        isHidePrev: true
    });

    popup.success({
        message: "Order Placed. Thank You..."
    });
    
    window.location = "index.html";

};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};

async function loadData() {

    const response = await fetch(
            "LoadCheckout"
            );
    if (response.ok) {

        const json = await response.json();

        if (json.success) {

            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;
            //load city
            let citySelect = document.getElementById("city");
            citySelect.length = 1;
            cityList.forEach(city => {

                let cityOption = document.createElement("option");
                cityOption.value = city.id;
                cityOption.innerHTML = city.name;
                citySelect.appendChild(cityOption);
            });

            //load current address
            let currnetAddressCheckbox = document.getElementById("checkbox1");
            currnetAddressCheckbox.addEventListener("change", e => {

                let first_name = document.getElementById("first-name");
                let last_name = document.getElementById("last-name");
                let city = document.getElementById("city");
                let address1 = document.getElementById("address1");
                let address2 = document.getElementById("address2");
                let postal_code = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");
                if (currnetAddressCheckbox.checked) {

                    first_name.value = address.first_name;
                    first_name.disabled = true;
                    last_name.value = address.last_name;
                    last_name.disabled = true;

                    city.value = address.city.id;
                    city.disabled = true;
                    city.dispatchEvent(new Event("change")); //balen event ekk call krn wdiy

                    address1.value = address.line1;
                    address1.disabled = true;
                    address2.value = address.line2;
                    address2.disabled = true;
                    postal_code.value = address.postal_code;
                    postal_code.disabled = true;
                    mobile.value = address.mobile;
                    mobile.disabled = true;
                } else {

                    first_name.value = "";
                    first_name.disabled = false;
                    last_name.value = "";
                    last_name.disabled = false;

                    city.value = 0;
                    city.disabled = false;
                    city.dispatchEvent(new Event("change"));

                    address1.value = "";
                    address1.disabled = false;
                    address2.value = "";
                    address2.disabled = false;
                    postal_code.value = "";
                    postal_code.disabled = false;
                    mobile.value = "";
                    mobile.disabled = false;
                }
            });

            //load cart items
            let st_tbody = document.getElementById("st-tbody");

            let st_item_tr = document.getElementById("st-item-tr");
            let st_order_subtotal_tr = document.getElementById("st-order-subtotal-tr");
            let st_order_shipping_tr = document.getElementById("st-order-shipping-tr");
            let st_order_total_tr = document.getElementById("st-order-total-tr");

            st_tbody.innerHTML = "";

            let sub_total = 0;

            cartList.forEach(item => {

                let st_item_clone = st_item_tr.cloneNode(true);
                st_item_clone.querySelector("#st-item-title").innerHTML = item.product.title;
                st_item_clone.querySelector("#st-item-qty").innerHTML = item.qty;

                let item_sub_total = item.product.price * item.qty;
                sub_total += item_sub_total;

                st_item_clone.querySelector("#st-item-subtotal").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(item_sub_total);

                st_tbody.appendChild(st_item_clone);

            });

            st_order_subtotal_tr.querySelector("#st-subtotal").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(sub_total);
            st_tbody.appendChild(st_order_subtotal_tr);

            citySelect.addEventListener("change", e => {

                //update shipping charges
                let item_count = cartList.length; //cart item count

                let shipping_amount = 0;

                //check city colombo or not

                if (citySelect.value == 1) {

                    //colombo
                    shipping_amount = item_count * 350;

                } else {

                    //out of colombo
                    shipping_amount = item_count * 500;
                }

                st_order_shipping_tr.querySelector("#st-shipping-amount").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(shipping_amount);
                st_tbody.appendChild(st_order_shipping_tr);

                //update total
                st_order_total_tr.querySelector("#st-total").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format((sub_total + shipping_amount));
                st_tbody.appendChild(st_order_total_tr);

            });

        } else {

            window.location = "sign-in.html";
        }

    }

}

async function checkout() {

    let isCurrentAddress = document.getElementById("checkbox1").checked;

    //get address
    let first_name = document.getElementById("first-name");
    let last_name = document.getElementById("last-name");
    let city = document.getElementById("city");
    let address1 = document.getElementById("address1");
    let address2 = document.getElementById("address2");
    let postal_code = document.getElementById("postal-code");
    let mobile = document.getElementById("mobile");

    //request data(json)
    const data = {
        isCurrentAddress: isCurrentAddress,
        first_name: first_name.value,
        last_name: last_name.value,
        city_id: city.value,
        address1: address1.value,
        address2: address2.value,
        postal_code: postal_code.value,
        mobile: mobile.value,
    };

    const response = await fetch(
            "Checkout",
            {

                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }

            }
    );

    const popup = Notification();
    popup.setProperty({
        duration: 5000,
        isHidePrev: true
    });

    if (response.ok) {

        const json = await response.json();

        if (json.success) {

            payhere.startPayment(json.payhereJson);

//            window.location = "index.html";

        } else {

            popup.error({
                message: json.message
            });

        }

    } else {

        popup.error({
            message: "Try again later"
        });

    }

}