# Recipes API Documentation

## Introduction

This API provides endpoints for managing recipes, their ingredients, and associated tags.

## Dependencies

- Java 17
- Maven 3.8.2
- Postgres 13
- Spring Boot 3
- Spring Data JPA
- Docker (optional)
- Docker Compose (optional)

### Up & Running

#### Via Docker Compose

Run the following command to start the API and its dependencies:

```bash

docker-compose up

```

#### Via Docker

Run the following commands to start the API and its dependencies:

First you have to run Postgres DB container:

```bash
docker run --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=recipes -p 5432:5432 -d postgres
```

Then you can run the API container:

```bash
git clone https://github.com/peacecwz/recipes-api.git
cd recipes-api
docker build -t recipes-api .
docker run -p 8000:8000 recipes-api
```

## API Documentation

### Recipes

#### Create a recipe

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Adana Kebab",
    "description": "The best kebab in the world. Adana Kebab is more spicy than other kebabs.",
    "servings": 2,
    "prepareTime": 25,
    "cookTime": 30,
    "instructions": "Prepare ground beef, onions and spices. Mix them and put them on the skewers. Cook them on the grill.",
    "tags": [
        "gluten-free"
    ],
    "ingredients": ["onion","meal"]
}'

```

Request Parameters

| Parameter      | Type      | Description                               |
|:---------------|:----------|:------------------------------------------|
| `name`         | `string`  | **Required**. Name of the recipe          |
| `description`  | `string`  | **Required**. Description of the recipe   |
| `servings`     | `integer` | **Required**. Number of servings          |
| `prepareTime`  | `integer` | **Required**. Preparation time in minutes |
| `cookTime`     | `integer` | **Required**. Cooking time in minutes     |
| `instructions` | `string`  | **Required**. Instructions of the recipe  |
| `tags`         | `array`   | **Required**. Tags of the recipe          |
| `ingredients`  | `array`   | **Required**. Ingredients of the recipe   |

#### Get all recipes

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes?term=kebap&tags=gluten-free&excludeIngredients=tomato&includeIngredients=potato'

```

Request Parameters

| Parameter            | Type     | Description                                                         |
|:---------------------|:---------|:--------------------------------------------------------------------|
| `term`               | `string` | **Optional**. Search term for recipes                               |
| `tags`               | `array`  | **Optional**. Tags of the recipe. e.g: Vegan, GlutenFree, Turkish   |
| `excludeIngredients` | `array`  | **Optional**. Ingredients that should not be included in the recipe |
| `includeIngredients` | `array`  | **Optional**. Ingredients that should be included in the recipe     |
| `servings`           | `number` | **Optional**. Serving people count                                  |

#### Get a recipe

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes/1'

```

#### Update a recipe

Send request via curl

```bash

curl --location --request PATCH 'http://localhost:8080/recipes/1' \
--header 'Content-Type: application/json' \
--data '[
    {
        "op": "replace",
        "path": "/description",
        "value": "Vanilla Cake"
    }
]'

```

Request Parameters

| Parameter | Type     | Description                                |
|:----------|:---------|:-------------------------------------------|
| `op`      | `string` | **Required**. Operation type. e.g: replace |
| `path`    | `string` | **Required**. Path of the field            |
| `value`   | `string` | **Required**. New value of the field       |

#### Delete a recipe

Send request via curl

```bash

curl --location --request DELETE 'http://localhost:8080/recipes/1'

```

### Ingredients

#### Create an ingredient

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes/1/ingredients' \
--header 'Content-Type: application/json' \
--data '{
    "name": "onion"
}'

```

Request Parameters

| Parameter | Type     | Description                          |
|:----------|:---------|:-------------------------------------|
| `name`    | `string` | **Required**. Name of the ingredient |

#### Get all ingredients

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes/1/ingredients'
```

#### Update an ingredient

Send request via curl

```bash

curl --location --request PUT 'http://localhost:8080/recipes/1/ingredients/102' \
--header 'Content-Type: application/json' \
--data '{
    "name": "kıyma"
}'

```

Request Parameters

| Parameter | Type     | Description                          |
|:----------|:---------|:-------------------------------------|
| `name`    | `string` | **Required**. Name of the ingredient |

#### Delete an ingredient

Send request via curl

```bash

curl --location --request DELETE 'http://localhost:8080/recipes/1/ingredients/102'

```

### Tags

#### Create a tag

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes/1/tags' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Vegan"
}'

```

Request Parameters

| Parameter | Type     | Description                   |
|:----------|:---------|:------------------------------|
| `name`    | `string` | **Required**. Name of the tag |

#### Get all tags

Send request via curl

```bash

curl --location 'http://localhost:8080/recipes/1/tags'
```

#### Update a tag

Send request via curl

```bash

curl --location --request PUT 'http://localhost:8080/recipes/1/tags/3' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Vegan"
}'

```

Request Parameters

| Parameter | Type     | Description                   |
|:----------|:---------|:------------------------------|
| `name`    | `string` | **Required**. Name of the tag |

#### Delete a tag

Send request via curl

```bash

curl --location --request DELETE 'http://localhost:8080/recipes/1/tags/3'

```

## License

[MIT](https://choosealicense.com/licenses/mit/)
