(ns bakery.products
  (:require
   [bakery.db :as db]
   [bakery.schema.product :as schema]
   [malli.core :as m]
   [next.jdbc.sql :as sql]))

(defonce products-db (atom []))
(defn reset-products-db! []
  (reset! products-db []))

(def plain-bagels
  {:name "Plain Bagels"
   :price_cents 1000
   :description "These Sourdough bagels are inspired by classic NY-style bagels - dense, chewy, and built for a proper bite! Made with barley malt syrup for subtle sweetness and depth, then boiled and baked for a glossy crust and firm chew. Intentionally simple - let the texture do the talking. 11/10."
   })

(def classic-loaf
  {:name "Classic Loaf"
   :price_cents 750
   :description "Naturally leavened with sourdough made with a blend of local bread flour and 20% whole wheat for depth, structure, and a gentle nutty finish. Long fermented for flavor and digestibility, baked dark with a crackly crust and soft, open crumb. The last best loaf you'll ever have."
   })

(def rosemary-bagels
  {:name "Rosemary Sea-salt Bagels"
   :price_cents 1100
   :description "NY-style inspired sourdough bagels with fresh rosemary mixed directly into the dough for an even, aromatic flavor throughout. Boiled, finished with flaky sea salt, then baked for a glossy crust and firm, classic chew. Made with barley malt syrup for depth and subtle sweetness. Best eaten in large quantities."})

(defn create-product! [product]
  (when-not (m/validate schema/ProductInput product)
    (throw (ex-info "Invalid product"
                        {:errors (m/explain schema/ProductInput product)})))

  (swap! products-db conj product))

(defn list-products []
  @products-db)
