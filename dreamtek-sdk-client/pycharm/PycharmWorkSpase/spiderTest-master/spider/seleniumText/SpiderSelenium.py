#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time     : 2019/5/15 17:26
# @Author  :  wancheng.b
# @Site     : 
# @File     : SpiderSelenium.py
# @Software  : PyCharm

'''
直接把chromedriver.exe放到路径下很方便
最好在chrom装一个插件方便查看xpath   装插件地址：https://blog.csdn.net/winter_dong/article/details/80819737

'''
import time

from selenium import webdriver
from selenium.webdriver import ActionChains
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

# browser = webdriver.Chrome()
#
# browser.get("http://www.baidu.com")
# browser.implicitly_wait(10)  # 隐式等待：全局等待，所有元素加载完毕才寻找目标元素，存在则继续操作，否则超过最大时间抛出异常
# time.sleep(2)
# browser.find_element_by_xpath('//*[@id="kw"]').send_keys('python')
# browser.find_element_by_xpath('//*[@id="su"]').click()
# browser.close()

'''
可以从源码里看出来By.ID就是'id'这个字符串
class By(object):
    """
    Set of supported locator strategies.
    """

    ID = "id"
    XPATH = "xpath"
    LINK_TEXT = "link text"
    PARTIAL_LINK_TEXT = "partial link text"
    NAME = "name"
    TAG_NAME = "tag name"
    CLASS_NAME = "class name"
    CSS_SELECTOR = "css selector"
'''


def find_element(browser, element, method='xpath'):
    try:
        WebDriverWait(browser, 10).until(EC.presence_of_element_located((method, element)))
        return browser.find_element(method, element)
    except Exception as e:
        print('no found element')
        raise e


# 显示等待：主要针对元素，元素出现则操作，超时则抛出异常，不用等到页面加载完毕
'''
断点调试：
F7进入断点执行的方法（shift F8：跳出F7进入的方法）， 可以一直进入
F8跳过断点，执行下一行代码， 
F9恢复程序（相当于不再是debug运行程序）
Alt + F9 运行游标
Alt + F8 验证表达式
Ctrl + Alt + F8 快速验证表达式
'''
browser = webdriver.Chrome()
browser.implicitly_wait(10)  # 隐式等待，等待浏览器加载完毕
browser.get('https://www.html5tricks.com/demo/jiaoben1454/index.html#')
element = find_element(browser, '/html/body/div[2]/ul/li[4]/a')

ActionChains(browser).move_to_element(element).perform()  # 鼠标移动到某个元素上
time.sleep(2)
print(browser.find_element_by_xpath('/html/body/div[2]/ul/li[4]/a/i').get_attribute('class'))
browser.close()

# *_*coding:utf-8*_*

# from selenium import webdriver
# from selenium.webdriver import ActionChains
# import time
#
# driver = webdriver.Chrome()
# driver.get('https://www.baidu.com/')
# #输入selenium 搜索
# driver.find_element_by_id('kw').send_keys('selenium')
# driver.find_element_by_id('su').click()
#
# #移动到 设置
# element = driver.find_element_by_name('tj_settingicon')
# ActionChains(driver).move_to_element(element).perform()
# time.sleep(2)
#
# #单击，弹出的Ajax
# driver.find_element_by_link_text('高级搜索').click()
