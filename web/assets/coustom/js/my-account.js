var modelList;

async function loadFeatures() {

    const response = await fetch(
            "LoadFeatures"
            );

    if (response.ok) {

        const json = await response.json();
        const categoryList = json.categoryList;
        modelList = json.modelList;
        const colorList = json.colorList;
        const storageList = json.storageList;
        const conditionList = json.conditionList;

        loadSelect("categorySelect", categoryList, "name");
        loadSelect("modelSelect", modelList, "name");
        loadSelect("colorSelect", colorList, "name");
        loadSelect("storageSelect", storageList, "value");
        loadSelect("conditionSelect", conditionList, "name");

    } else {

        document.getElementById("message").innerHTML = "Please try again later!";
    }

}

function loadSelect(selectTagId, list, property) {

    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {

        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item[property];
        selectTag.appendChild(optionTag);

    });

}

function updateModels() {

    let modelSelectTag = document.getElementById("modelSelect");
    modelSelectTag.length = 1; //select tag ekak length = 0 kroth okkom remove wenw

    let selectedcategoryId = document.getElementById("categorySelect").value;

    modelList.forEach(model => {
        if (model.category.id == selectedcategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelSelectTag.appendChild(optionTag);
        }
    });

}

//async function productListning() {
//
//    const categoryTag = document.getElementById("categorySelect");
//    const modelTag = document.getElementById("modelSelect");
//    const titleTag = document.getElementById("title");
//    const descriptionTag = document.getElementById("description");
//    const storageTag = document.getElementById("storageSelect");
//    const colorTag = document.getElementById("colorSelect");
//    const conditionTag = document.getElementById("conditionSelect");
//    const priceTag = document.getElementById("price");
//    const quantityTag = document.getElementById("quantity");
//    const image1Tag = document.getElementById("image1");
//    const image2Tag = document.getElementById("image2");
//    const image3Tag = document.getElementById("image3");
//
//    const data = new FormData();
//    data.append("categoryId", categoryTag.value);
//    data.append("modelId", modelTag.value);
//    data.append("title", titleTag.value);
//    data.append("description", descriptionTag.value);
//    data.append("storageId", storageTag.value);
//    data.append("colorId", colorTag.value);
//    data.append("conditionId", conditionTag.value);
//    data.append("price", priceTag.value);
//    data.append("quantity", quantityTag.value);
//    data.append("image1", image1Tag.files[0]);
//    data.append("image2", image2Tag.files[0]);
//    data.append("image3", image3Tag.files[0]);
//
//    const response = await fetch(
//            "ProductListing",
//            {
//                method: "POST",
//                body: data
//
//            }
//    );
//
//    if (response.ok) {
//
//        const json = await response.json();
//
//        const popup = Notification();
//        popup.setProperty({
//            duration: 3000,
//            isHidePrev: true
//        });
//
////        const messageTag = document.getElementById("message");
//
//        if (json.success) {
//
//            categoryTag.value = 0;
//            modelTag.length = 1;
//            titleTag.value = "";
//            descriptionTag.value = "";
//            storageTag.value = 0;
//            colorTag.value = 0;
//            conditionTag.value = 0;
//            priceTag.value = "";
//            quantityTag.value = 1;
//            image1Tag.value = null;
//            image2Tag.value = null;
//            image3Tag.value = null;
//
////            messageTag.innerHTML = json.content;
////            messageTag.className = "text-success";
//
//            popup.success({
//                message: json.content
//            });
//
//        } else {
//
//            popup.error({
//                message: json.content
//            });
//
////            messageTag.innerHTML = json.content;
////            messageTag.className = "text-danger";
//
////            Swal.fire({
////                title: 'Error!',
////                text: json.content,
////                icon: 'error',
////                confirmButtonText: 'Cool'
////            })
//        }
//
//    } else {
////        document.getElementById("message").innerHTML = "Please try again later!";
//    }
//
//}

async function productListning() {

    const categoryTag = document.getElementById("categorySelect");
    const modelTag = document.getElementById("modelSelect");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const storageTag = document.getElementById("storageSelect");
    const colorTag = document.getElementById("colorSelect");
    const conditionTag = document.getElementById("conditionSelect");
    const priceTag = document.getElementById("price");
    const quantityTag = document.getElementById("quantity");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");
    
    // Ensure there's an element with id 'message'
    const messageTag = document.getElementById("message");

    const data = new FormData();
    data.append("categoryId", categoryTag.value);
    data.append("modelId", modelTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("storageId", storageTag.value);
    data.append("colorId", colorTag.value);
    data.append("conditionId", conditionTag.value);
    data.append("price", priceTag.value);
    data.append("quantity", quantityTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);

    try {
        const response = await fetch("ProductListing", {
            method: "POST",
            body: data
        });

        if (response.ok) {
            const json = await response.json();
            const popup = Notification();
            popup.setProperty({ duration: 3000, isHidePrev: true });

            if (json.success) {
                // Reset form values
                categoryTag.value = 0;
                modelTag.length = 1;
                titleTag.value = "";
                descriptionTag.value = "";
                storageTag.value = 0;
                colorTag.value = 0;
                conditionTag.value = 0;
                priceTag.value = "";
                quantityTag.value = 1;
                image1Tag.value = null;
                image2Tag.value = null;
                image3Tag.value = null;

                popup.success({ message: json.content });
            } else {
                popup.error({ message: json.content });
            }
        } else {
            // Show error message for bad response
            if (messageTag) {
                messageTag.innerHTML = "Please try again later!";
            }
        }
    } catch (error) {
        // Handle any errors that may occur during the fetch call
        if (messageTag) {
            messageTag.innerHTML = "An error occurred. Please try again!";
        }
        console.error('Fetch error:', error);
    }
}
