//
//  GameDataSets.swift
//  Hangman
//
//  Created by Govindaraju, Saranath on 6/14/15.
//  Copyright (c) 2015 Saranath Govindaraju. All rights reserved.
//

import Foundation

class GameModel
{
    
    private struct DataSets{
         static var data =   [ "aardvark" , "addax" , "alligator" , "alpaca" , "anteater" , "antelope" , "aoudad" , "ape" , "argali" , "armadillo" , "ass" , "baboon" , "badger" , "basilisk" , "bat" , "bear" , "beaver" , "bighorn" , "bison" , "boar" , "budgerigar" , "buffalo" , "bull" , "bunny" , "burro" , "camel" , "canary" , "capybara" , "cat" , "chameleon" , "chamois" , "cheetah" , "chimpanzee" , "chinchilla" , "chipmunk" , "civet" , "coati" , "colt" , "cony" , "cougar" , "cow" , "coyote" , "crocodile" , "crow" , "deer" , "dingo" , "doe" , "dog" , "donkey" , "dormouse" , "dromedary" , "duckbill" , "dugong" , "eland" , "elephant" , "elk" , "ermine" , "ewe" , "fawn" , "ferret" , "finch" , "fish" , "fox" , "frog" , "gazelle" , "gemsbok" , "gila" , "monster" , "giraffe" , "gnu" , "goat" , "gopher" , "gorilla" , "grizzly" , "bear" , "ground" , "hog" , "guanaco" , "guinea" , "pig" , "hamster" , "hare" , "hartebeest" , "hedgehog" , "hippopotamus" , "hog" , "horse" , "hyena" , "ibex" , "iguana" , "impala" , "jackal" , "jaguar" , "jerboa" , "kangaroo" , "kid" , "kinkajou" , "kitten" , "koala" , "koodoo" , "lamb" , "lemur" , "leopard" , "lion" , "lizard" , "llama" , "lovebird" , "lynx" , "mandrill" , "mare" , "marmoset" , "marten" , "mink" , "mole" , "mongoose" , "monkey" , "moose" , "mountain" , "goat" , "mouse" , "mule" , "musk" , "deer" , "musk-ox" , "muskrat" , "mustang" , "mynah" , "bird" , "newt" , "ocelot" , "okapi" , "opossum" , "orangutan" , "oryx" , "otter" , "ox" , "panda" , "panther" , "parakeet" , "parrot" , "peccary" , "pig" , "platypus" , "polar" , "bear" , "pony" , "porcupine" , "porpoise" , "prairie" , "dog" , "pronghorn" , "puma" , "puppy" , "quagga" , "rabbit" , "raccoon" , "ram" , "rat" , "reindeer" , "reptile" , "rhinoceros" , "roebuck" , "salamander" , "seal" , "sheep" , "shrew" , "silver" , "fox" , "skunk" , "sloth" , "snake" , "springbok" , "squirrel" , "stallion" , "steer" , "tapir" , "tiger" , "toad" , "turtle" , "vicuna" , "walrus" , "warthog" , "waterbuck" , "weasel" , "whale" , "wildcat" , "wolf" , "wolverine" , "wombat" , "woodchuck" , "yak", "zebra", "zebu"]
        
        static var usedWords = [String]()
    }
    
    private func generateRandomNumber(n: UInt32) -> Int    { return Int(arc4random_uniform(n))    }
    
    
    func getWord() -> String    {
        var n: UInt32 = UInt32(DataSets.data.count)
        var temp = ""
        if DataSets.usedWords.count == DataSets.data.count {
            DataSets.usedWords.removeAll(keepCapacity: false)
        }
        do {
            temp = DataSets.data[generateRandomNumber(n)]
        } while contains(DataSets.usedWords, temp)
        return temp
    }
    
    
}