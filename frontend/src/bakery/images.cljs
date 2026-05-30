(ns bakery.images)

(def images-root "images/")

(def hero-images
  ["two-loaves.jpeg"
   "bread-cross-section.jpeg"
   "bread-and-bagels.JPG"
   "bagel.jpg"])

(def hero-reel
  (map #(str images-root %) hero-images))
