window.onload = async  function checkSignIn() {

    const response = await fetch(
            "CheckSignIn",
            );

    if (response.ok) {
        const json = await  response.json();
        console.log(json);

        const response_dto = json.response_dto;



        if (response_dto.success) {
            //sign in
            const user = response_dto.content;




            let st_button_1 = document.getElementById("st-button-1");

            st_button_1.href = "SignOut";

            document.getElementById("st-button-2").innerHTML = user.firstname + " " + user.lastname;
            document.getElementById("st-button-3").innerHTML = "SignOut";



        } else {
            //not sign in

        }
        //display last 3 products

        const productList = json.products;

        let i = 1;
        productList.forEach(product => {
            document.getElementById("st-product-title-" + i).innerHTML = product.title;
            document.getElementById("single-product-link-" + i).href = "shop-single.html?id=" + product.id;
            document.getElementById("product-img-" + i).src = "product-images/" + product.id + "/" + product.id + "image1.png";
            document.getElementById("st-product-price-" + i).innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }

            ).format((product.price));
            i++;
        });
//        // banner carousel active 
        $('.banner-slide__active').slick({
            infinite: true,
            speed: 500,
            slidesToShow: 3,
            autoplay: true,
            autoplaySpeed: 3000,
            slidesToScroll: 1,
            dots: false,
            arrows: true,
            prevArrow: '<i class="slick-arrow slick-prev far fa-long-arrow-left"></i>',
            nextArrow: '<i class="slick-arrow slick-next far fa-long-arrow-right"></i>',
            responsive: [
                {
                    breakpoint: 1025,
                    settings: {
                        slidesToShow: 3,
                    }
                },
                {
                    breakpoint: 769,
                    settings: {
                        slidesToShow: 2,
                    }
                },
                {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 1,
                    }
                }
            ]
        });

        const productList2 = json.products;

        let j = 1;
        productList2.forEach(product => {
            document.getElementById("st-producth-title-" + j).innerHTML = product.title;
            document.getElementById("single-producth-link-" + j).href = "shop-single.html?id=" + product.id;
            document.getElementById("producth-img-" + j).src = "product-images/" + product.id + "/" + product.id + "image1.png";
            document.getElementById("st-producth-price-" + j).innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }

            ).format((product.price));
            i++;
        });

    }


}

//including pages  shoud include cart.js and notification.js to the index.html
async  function viewCart() {
    const response = await  fetch("cart.html");
    if (response.ok) {
        const cartHtmlText = await  response.text();

        const paser = new DOMParser();
        const cartHtml = paser.parseFromString(cartHtmlText, "text/html");

        const cart_main = cartHtml.querySelector(".main-wrapper");

        document.querySelector(".main-wrapper").innerHTML = cart_main.innerHTML;


        loadCartItems();

        console.log(cart_main);
    }
}

