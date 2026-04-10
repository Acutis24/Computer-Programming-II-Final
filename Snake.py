from graphics import *
import time
import random

#Graphic window

win = GraphWin("Snakegame", 400, 400)
win.setCoords(0,0,400,400)



def StartMenu():
    # Background
    background = Rectangle(Point(0,0), Point(400,400))
    background.setFill("black")
    background.setOutline("black")
    background.draw(win)

    # Title
    title = Text(Point(200, 260), "SNAKE GAME")
    title.setSize(28)
    title.setFill("green")
    title.draw(win)

    # Instructions
    instructions = Text(Point(200, 200), "Press SPACE or Enter to Start")
    instructions.setSize(16)
    instructions.setFill("white")
    instructions.draw(win)

    controls = Text(Point(200, 160), "Use W A S D to Move")
    controls.setSize(14)
    controls.setFill("white")
    controls.draw(win)

    while True:
        key = win.checkKey().lower()
        if key == "space" or key == "return":
            break

#GAME OVER
def GameOverScreen(message, score):

    background = Rectangle(Point(0,0), Point(400,400))
    background.setFill("black")
    background.setOutline("red")
    background.draw(win)

    text = Text(Point(200,250), "GAME OVER")
    text.setSize(24)
    text.setFill("red")
    text.draw(win)

    reason = Text(Point(200, 200), message)
    reason.setSize(16)
    reason.setFill("white")
    reason.draw(win)

    finalscore = Text(Point(200, 90), f"Final Score: {score}")
    finalscore.setSize(16)
    finalscore.setFill("yellow")
    finalscore.draw(win)

    prompt = Text(Point(200, 150), "Press R to Restart or Q to Quit")
    prompt.setSize(14)
    prompt.setFill("white")
    prompt.draw(win)

    while True:
        key = win.checkKey().lower()
        if key == "r":
            win.close
            StartGame()
        elif key == "q":
            win.close()
            exit()

#The Game
def StartGame():
    global win

    #Checker Board
    SquareSize = 25

    Color1 = "Grey"
    Color2 = "Black"

    for row in range(16):
        for col in range(16):
            x1 = col * SquareSize
            y1 = row * SquareSize
            x2 = x1 + SquareSize
            y2 = y1 + SquareSize

            R = Rectangle(Point(x1,y1), Point(x2,y2))

            if (row + col) % 2 == 0:
                R.setFill(Color1)
            else:
                R.setFill(Color2)

            R.draw(win)

    score = 0
    scoreText = Text(Point(200, 385), f"Score: {score}")
    scoreText.setSize(14)
    scoreText.setFill("yellow")
    scoreText.draw(win)

    #Snake


    SnakeColor = "Green"

    SnakeX = 0
    SnakeY = 0
    SnakePos = Point(SnakeX,SnakeY)
    SnakePos2 = Point(SnakeX + SquareSize, SnakeY + SquareSize)
    SnakeHead = Rectangle(SnakePos, SnakePos2)
    SnakeHead.setFill(SnakeColor)
    SnakeHead.draw(win)

    #Fruit Spawning
    step = SquareSize

    SnakemoveX = 0
    SnakemoveY = 0

    def positionInSnake(x, y, SnakeBody, SnakeHead):

        HeadP1 = SnakeHead.getP1()
        if HeadP1.getX() == x and HeadP1.getY() == y: 
            return True

        for tail in SnakeBody:
            tailP1 = tail.getP1()
            if tailP1.getX() == x and tailP1.getY() == y:
                return True
        return False

    def FruitSpawn():
        while True:
            fruitx = random.randint(0, 15) * SquareSize
            fruity = random.randint(0, 15) * SquareSize

            if not positionInSnake(fruitx, fruity, SnakeBody, SnakeHead):
                break

        fruit = Rectangle(
            Point(fruitx, fruity), Point(fruitx + SquareSize, fruity + SquareSize))
        fruit.setFill("Red")
        fruit.draw(win)
        return fruit

    #Snake Body
    SnakeBody = []

    fruit = FruitSpawn()



        
    #Snake Direction
    SnakeDirection = "none"

    #Snake movement
    while True:
        key = win.checkKey()
        if key == "w" and SnakeDirection != "down": 
            SnakemoveX = 0
            SnakemoveY = step
            SnakeDirection = "up"
        elif key == "a" and SnakeDirection != "right":
            SnakemoveX = -step
            SnakemoveY = 0
            SnakeDirection = "left"
        elif key == "d" and SnakeDirection != "left":
            SnakemoveX = step
            SnakemoveY = 0
            SnakeDirection = "right"
        elif key == "s" and SnakeDirection != "up":
            SnakemoveX = 0
            SnakemoveY = -step
            SnakeDirection = "down"

        SnakePos = SnakeHead.getP1()
        SnakePos2 = SnakeHead.getP2()

        SnakeHead.move(SnakemoveX,SnakemoveY)
        time.sleep(0.15)

    #Wall Detection
        HeadP1 = SnakeHead.getP1()
        HeadP2 = SnakeHead.getP2()
        if HeadP1.getX() < 0 or HeadP2.getX() > 400 or HeadP1.getY() < 0 or HeadP2.getY() > 400:
            GameOverScreen("Game Over: Crash", score)
            break
    #Snaketail

        tail = Rectangle(Point(HeadP1.getX(), HeadP1.getY()), Point(HeadP2.getX(), HeadP2.getY()))
        tail.setFill(SnakeColor)
        tail.draw(win)
        SnakeBody.append(tail)


    #Self Collision

        for tail in SnakeBody[:-1]: 

            tailP1 = tail.getP1()

            if (tailP1.getX() == HeadP1.getX() and tailP1.getY() == HeadP1.getY()):
                GameOverScreen("Game Over: Tail Bite", score)
                win.close()
                exit()

    #Fruit eating
        FruitEaten = False
        HeadP1 = SnakeHead.getP1()

        if (HeadP1.getX() == fruit.getP1().getX() and HeadP1.getY() == fruit.getP1().getY()):
            fruit.undraw()
            fruit = FruitSpawn()
            FruitEaten = True

            score += 1
            scoreText.setText(f"Score: {score}")

        if not FruitEaten:
            tail = SnakeBody.pop(0)
            tail.undraw()
    #Play again?
    answer = input("Want to Play again?: ").lower()

    if answer == "y" or answer == "yes":
        win.close()
        StartGame()
    else:
        win.close()
        exit()

StartMenu()
StartGame()