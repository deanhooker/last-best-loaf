(ns last-best-loaf.db)

(def default-db
  {:route nil

   :catalog
   {:items
    {:classic-loaf {:id :classic-loaf
                    :name "Classic Loaf"
                    :price 7.50
                    :description "Naturally leavened with sourdough made with a blend of local bread flour and 20% whole wheat for depth, structure, and a gentle nutty finish. Long fermented for flavor and digestibility, baked dark with a crackly crust and soft, open crumb. 
                    The last best loaf you'll ever have."}

     :plain-bagels {:id :plain-bagels
                    :name "Plain Bagels"
                    :price 10
                    :description "These Sourdough bagels are inspired by classic NY-style bagels - dense, chewy, and built for a proper bite! Made with barley malt syrup for subtle sweetness and depth, then boiled and baked for a glossy crust and firm chew. Intentionally simple - let the texture do the talking. 
11/10"}

     :rosemary-bagels {:id :rosemary-bagels
                       :name "Rosemary Sea-salt Bagels"
                       :price 11
                       :description "NY-style inspired sourdough bagels with fresh rosemary mixed directly into the dough for an even, aromatic flavor throughout. Boiled, finished with flaky sea salt, then baked for a glossy crust and firm, classic chew. 
Made with barley malt syrup for depth and subtle sweetness. 
Best eaten in large quantities"}}}

   :cart {:items {}}

   :checkout
   {:fulfillment :pickup   ;; :pickup | :delivery
    :pickup-date ""
    :pickup-window ""
    :delivery-address ""
    :notes ""}

   :customer
   {:name ""
    :email ""
    :phone ""}

   :bake-days nil

   :ui
   {:errors {}
    :submitting? false}
   })
