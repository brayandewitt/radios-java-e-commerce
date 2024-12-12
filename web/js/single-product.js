



window.onload = async function loadProduct() {
   
    const parameters = new URLSearchParams(window.location.search);
    if (parameters.has("id")) {
        const productId = parameters.get("id");
       
        const response = await fetch("LoadSingleProduct?id=" + productId);


        if (response.ok) {
            const json = await response.json();
            console.log(json);

            const id = json.product.id;
            document.getElementById("image1").src = "product-images/" + id + "/" + id + "image1.png";
            document.getElementById("image2").src = "product-images/" + id + "/" + id + "image2.png";
            document.getElementById("image3").src = "product-images/" + id + "/" + id + "image3.png";


            document.getElementById("image1-thumb").src = "product-images/" + id + "/" + id + "image1.png";
            document.getElementById("image2-thumb").src = "product-images/" + id + "/" + id + "image2.png";
            document.getElementById("image3-thumb").src = "product-images/" + id + "/" + id + "image3.png";

            document.getElementById("product-title").innerHTML = json.product.title;
            
            document.getElementById("product-published-on").innerHTML = json.product.date_time;
            
            document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }

            ).format(json.product.price);
            document.getElementById("product-description").innerHTML = json.product.description;
            document.getElementById("product-category").innerHTML = json.product.model.category.name;
            document.getElementById("product-model").innerHTML = json.product.model.name;
            document.getElementById("product-storage").innerHTML = json.product.storage.value;
            
            document.getElementById("product-condition").innerHTML = json.product.product_condition.name;
            document.getElementById("product-qty").innerHTML = json.product.qty;

            document.getElementById("color-border").style.backgroundColor = json.product.color.name;
            document.getElementById("color-name").innerHTML = json.product.color.name;

            
            




//            ***************
            document.getElementById("add-to-cart-main").addEventListener(
                    "click",
                    (e) => {
                addToCart(
                        json.product.id,
                        document.getElementById("add-to-cart-qty").value
                        );
                e.preventDefault();
            });

            let ProductHtml = document.getElementById("similer-product");
            document.getElementById("similer-product-main").innerHTML = "";

            json.productList.forEach(item => { 

                let productCloneHtml = ProductHtml.cloneNode(true);

                productCloneHtml.querySelector("#similer-product-a1").href = "shop-single.html?id=" + item.id;
                productCloneHtml.querySelector("#similer-product-image").src = "product-images/" + item.id + "/" + item.id + "image1.png";
                productCloneHtml.querySelector("#similer-product-a2").href = "shop-single.html?id=" + item.id;
                productCloneHtml.querySelector("#similer-product-title").innerHTML = item.title;
                productCloneHtml.querySelector("#similer-product-storage").innerHTML = item.storage.value;
              productCloneHtml.querySelector("#similer-product-qty").innerHTML = item.qty;
                productCloneHtml.querySelector("#similer-product-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                ).format(item.price);
               // productCloneHtml.querySelector("#similer-product-color-border").style.borderColor = item.color.name;
                //.querySelector("#similer-product-color").style.backgroundColor = item.color.name;

                productCloneHtml.querySelector("#similer-product-add-to-cart").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                });

                //change other tags

                document.getElementById("similer-product-main").appendChild(productCloneHtml);

            });

            

        } else {
            window.location = "index.html";
        }

    } else {
        window.location = "index.html";
        }
}

//function addToCart(id,qty) {
//
//    console.log("add to cart id: " + id);
//    console.log("add to cart qty: " + qty);
//
//}

async function addToCart(id, qty) {
    const popup = Notification();
    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty + "",
            );

    if (response.ok) {

        const json = await response.json();
        if (json.success) {
            popup.success({

                message: json.content
            });
        } else {
            popup.error({

                message: json.content
            });
        }

    } else {
        //document.getElementById("message").innerHTML = "Please try again later!";
        popup.error({

            message: "unable to process your request"
        });
        }

}